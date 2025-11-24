const API_URL = "http://localhost:8080/Gerenciador-Tarefas";

// --- ELEMENTOS GLOBAIS ---
const divLista = document.getElementById("tasksList");
const modalForm = document.getElementById("modal");
const modalDelete = document.getElementById("modalDelete");
const form = document.getElementById("taskForm");
let idParaExcluir = null;

// --- FUNÇÕES DE DATA ---
function formatarDataParaJava(dataHtml) {
    if (!dataHtml) return null;
    const [ano, mes, dia] = dataHtml.split("-");
    return `${dia}/${mes}/${ano}`;
}

function formatarDataParaHtml(dataJava) {
    if (!dataJava) return "";
    const [dia, mes, ano] = dataJava.split("/");
    return `${ano}-${mes}-${dia}`;
}

function estaAtrasada(dataStringJava) {
    if (!dataStringJava) return false;
    const [dia, mes, ano] = dataStringJava.split("/");
    const dataPrazo = new Date(ano, mes - 1, dia);
    const dataHoje = new Date();
    dataHoje.setHours(0,0,0,0);
    return dataPrazo < dataHoje;
}

// --- MENSAGEM NA TELA ---
function mostrarMensagem(texto, isErro = false) {
    const msgDiv = document.getElementById("statusMsg");
    if(!msgDiv) return;

    msgDiv.innerText = texto;
    msgDiv.classList.remove("hidden", "status-success", "status-error");

    if (isErro) {
        msgDiv.classList.add("status-error");
    } else {
        msgDiv.classList.add("status-success");
    }
}

// --- RENDERIZAÇÃO ---
async function carregarTarefas() {
    try {
        const res = await fetch(API_URL);
        const tarefas = await res.json();

        divLista.innerHTML = "";

        tarefas.forEach(t => {
            const atrasada = estaAtrasada(t.dataTermino);
            const badgeHtml = atrasada ? `<div class="badge-late">⚠️ Atrasada</div>` : '';

            const card = document.createElement("div");
            card.className = "task-card";

            card.innerHTML = `
                <div class="task-header">
                    <div class="task-title">${t.titulo}</div>
                    ${badgeHtml}
                </div>
                <div class="task-meta">👤 ${t.responsavel}</div>
                <div class="task-meta">📅 ${t.dataTermino}</div>
                <div class="task-desc">${t.detalhamento || '<i>Sem descrição.</i>'}</div>
                <div class="card-actions">
                    <button class="btn-card btn-alterar" onclick="prepararEdicao(${t.id})">Editar</button>
                    <button class="btn-card btn-excluir" onclick="confirmarExclusao(${t.id})">Excluir</button>
                </div>
            `;
            divLista.appendChild(card);
        });
    } catch (erro) {
        console.error("Erro ao carregar:", erro);
        divLista.innerHTML = "<p style='text-align:center; margin-top:20px; color:gray'>Erro de conexão.</p>";
    }
}

// --- BOTÕES E MODAL ---

// Botão Nova Tarefa
const btnNew = document.getElementById("btnNew");
if (btnNew) {
    btnNew.onclick = () => {
        form.reset();
        document.getElementById("id").value = "";

        // --- FAXINA COMPLETA (Adicionado aqui também) ---
        const msgDiv = document.getElementById("statusMsg");
        if(msgDiv) {
            msgDiv.classList.add("hidden");
            msgDiv.classList.remove("status-success", "status-error");
            msgDiv.innerText = "";
        }
        // ------------------------------------------------

        modalForm.classList.remove("hidden");
    };
}

// Botão Voltar
const btnCancel = document.getElementById("btnCancel");
if (btnCancel) {
    btnCancel.onclick = () => modalForm.classList.add("hidden");
}

// Enviar Formulário
if (form) {
    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const id = document.getElementById("id").value;
        const tarefaObjeto = {
            titulo: document.getElementById("titulo").value,
            responsavel: document.getElementById("responsavel").value,
            dataTermino: formatarDataParaJava(document.getElementById("dataTermino").value),
            detalhamento: document.getElementById("detalhamento").value
        };

        try {
            let metodo = "POST";
            let url = API_URL;

            if (id) {
                metodo = "PUT";
                url = `${API_URL}/${id}`;
            }

            const res = await fetch(url, {
                method: metodo,
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(tarefaObjeto)
            });

            if (res.ok) {
                // SUCESSO
                mostrarMensagem("Salvo com sucesso!", false);
                setTimeout(() => {
                    modalForm.classList.add("hidden");
                    carregarTarefas();
                    const msgDiv = document.getElementById("statusMsg");
                    if(msgDiv) msgDiv.classList.add("hidden");
                }, 1500);
            } else {
                // ERRO
                const text = await res.text();
                let msgFinal = "";

                try {
                    const json = JSON.parse(text);
                    if (json.message) {
                        msgFinal = json.message;
                    } else {
                        msgFinal = JSON.stringify(json);
                    }
                } catch (e) {
                    msgFinal = text;
                }

                if (!msgFinal) msgFinal = "Erro desconhecido ao salvar.";
                mostrarMensagem("⚠️ " + msgFinal, true);
            }

        } catch (erro) {
            console.error("Erro ao salvar:", erro);
            mostrarMensagem("❌ Erro de conexão.", true);
        }
    });
}

// Preparar Edição
window.prepararEdicao = async (id) => {
    try {
        const res = await fetch(API_URL);
        const tarefas = await res.json();
        const tarefa = tarefas.find(t => t.id === id);

        if (tarefa) {
            document.getElementById("id").value = tarefa.id;
            document.getElementById("titulo").value = tarefa.titulo;
            document.getElementById("responsavel").value = tarefa.responsavel;
            document.getElementById("detalhamento").value = tarefa.detalhamento;
            document.getElementById("dataTermino").value = formatarDataParaHtml(tarefa.dataTermino);

            // --- FAXINA AO EDITAR ---
            const msgDiv = document.getElementById("statusMsg");
            if(msgDiv) {
                msgDiv.classList.add("hidden");
                msgDiv.classList.remove("status-success", "status-error");
                msgDiv.innerText = "";
            }

            modalForm.classList.remove("hidden");
        }
    } catch (erro) {
        console.error("Erro ao buscar:", erro);
    }
};

// --- EXCLUSÃO ---
window.confirmarExclusao = (id) => {
    idParaExcluir = id;
    document.getElementById("msgDelete").innerText = `Tem certeza que deseja apagar a tarefa ${id}?`;
    modalDelete.classList.remove("hidden");
};

const btnConfirmDelete = document.getElementById("btnConfirmDelete");
if (btnConfirmDelete) {
    btnConfirmDelete.addEventListener("click", async () => {
        if (idParaExcluir) {
            try {
                const res = await fetch(`${API_URL}/${idParaExcluir}`, { method: "DELETE" });
                if (res.ok) carregarTarefas();
                else alert("Erro ao excluir.");

                modalDelete.classList.add("hidden");
                idParaExcluir = null;
            } catch (error) { console.error(error); }
        }
    });
}

const btnCancelDelete = document.getElementById("btnCancelDelete");
if (btnCancelDelete) {
    btnCancelDelete.addEventListener("click", () => {
        modalDelete.classList.add("hidden");
        idParaExcluir = null;
    });
}

// Inicializa
carregarTarefas();