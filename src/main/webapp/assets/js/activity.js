const questionsList = document.getElementById("questions-list");
const addQuestionButton = document.getElementById("add-question");
const WEIGHT_EPSILON = 0.0001;

function updateQuestionTitles() {
    const questions = questionsList.querySelectorAll(".question-item");

    questions.forEach((question, index) => {
        const title = question.querySelector(".question-number");
        title.textContent = `Questão ${index + 1}`;
        title.dataset.number = index + 1;
        question.querySelector(".remove-question").disabled = questions.length === 1;
    });
}

function updateQuestionType(question) {
    if (!question) {
        return;
    }

    const type = question.querySelector(".question-type")?.value;
    const openFields = question.querySelector(".open-question-fields");
    const closedFields = question.querySelector(".closed-question-fields");

    if (!openFields || !closedFields) {
        return;
    }

    openFields.classList.toggle("d-none", type === "FECHADA");
    closedFields.classList.toggle("d-none", type !== "FECHADA");
}

function bindQuestionTypeFields(root = document) {
    root.querySelectorAll(".question-type").forEach((typeField) => {
        updateQuestionType(typeField.closest(".question-item, form"));
    });
}

function parseWeight(value) {
    const weight = parseFloat((value || "").replace(",", "."));
    return Number.isNaN(weight) ? 0 : weight;
}

function sumQuestionWeights(form) {
    return Array.from(form.querySelectorAll('input[name="questionWeight"]'))
        .reduce((total, input) => total + parseWeight(input.value), 0);
}

function validateActivityWeightLimit(form) {
    const activityWeightInput = form.querySelector('input[name="weight"]');

    if (!activityWeightInput) {
        return true;
    }

    const activityWeight = parseWeight(activityWeightInput.value);
    const questionsWeight = sumQuestionWeights(form);

    if (questionsWeight - activityWeight > WEIGHT_EPSILON) {
        alert(`A soma dos pesos das questões (${questionsWeight.toFixed(1)}) não pode ultrapassar o peso da atividade (${activityWeight.toFixed(1)}).`);
        return false;
    }

    return true;
}

function createQuestionItem() {
    const question = document.createElement("div");
    question.className = "question-item";
    question.innerHTML = `
        <div class="question-item-header">
            <strong class="question-number" data-number="">Questão</strong>
            <button type="button" class="btn btn-sm btn-action btn-action-delete remove-question">
                <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                    <path d="M9 3h6l1 2h4v2H4V5h4l1-2Zm-2 6h10l-.7 12H7.7L7 9Zm2.1 2 .5 8h1.8l-.4-8H9.1Zm3.9 0v8h2v-8h-2Z"/>
                </svg>
                Remover
            </button>
        </div>
        <div class="row g-3">
            <div class="col-md-3">
                <label class="form-label">Tipo</label>
                <select class="form-control question-type" name="questionType">
                    <option value="ABERTA">Aberta</option>
                    <option value="FECHADA">Fechada</option>
                </select>
            </div>
            <div class="col-md-9">
                <label class="form-label">Enunciado</label>
                <textarea class="form-control" name="questionText" rows="3" placeholder="Digite o enunciado da questão" required></textarea>
            </div>
            <div class="col-md-3">
                <label class="form-label">Peso</label>
                <input type="number" class="form-control" name="questionWeight" min="0" step="0.1" placeholder="Ex: 1.0">
            </div>
            <div class="col-md-9 open-question-fields">
                <label class="form-label">Resposta esperada</label>
                <textarea class="form-control" name="expectedAnswer" rows="2" placeholder="Resposta que ficará salva apenas para o professor"></textarea>
            </div>
            <div class="col-12 closed-question-fields d-none">
                <div class="row g-3">
                    <div class="col-md-3">
                        <label class="form-label">Alternativa A</label>
                        <input type="text" class="form-control" name="optionA">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">Alternativa B</label>
                        <input type="text" class="form-control" name="optionB">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">Alternativa C</label>
                        <input type="text" class="form-control" name="optionC">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">Alternativa D</label>
                        <input type="text" class="form-control" name="optionD">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">Letra correta</label>
                        <select class="form-control" name="correctOption">
                            <option value="A">A</option>
                            <option value="B">B</option>
                            <option value="C">C</option>
                            <option value="D">D</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
    `;

    return question;
}

if (addQuestionButton && questionsList) {
    addQuestionButton.addEventListener("click", () => {
        questionsList.appendChild(createQuestionItem());
        updateQuestionTitles();
    });

    questionsList.addEventListener("click", (event) => {
        const removeButton = event.target.closest(".remove-question");

        if (!removeButton) {
            return;
        }

        const questions = questionsList.querySelectorAll(".question-item");

        if (questions.length > 1) {
            removeButton.closest(".question-item").remove();
            updateQuestionTitles();
        }
    });

    questionsList.addEventListener("change", (event) => {
        const typeField = event.target.closest(".question-type");

        if (typeField) {
            updateQuestionType(typeField.closest(".question-item"));
        }
    });

    updateQuestionTitles();
    questionsList.querySelectorAll(".question-item").forEach(updateQuestionType);
}

document.addEventListener("change", (event) => {
    const typeField = event.target.closest(".question-type");

    if (typeField) {
        updateQuestionType(typeField.closest(".question-item, form"));
    }
});

bindQuestionTypeFields();

function fileToBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result.split(',')[1]);
        reader.onerror = error => reject(error);
    });
}

async function generateQuestionsAI() {
    
    const materialText = document.getElementById('aiMaterial').value;
    const fileInput = document.getElementById('aiFile');
    const type = document.getElementById('aiType').value;
    const quantity = document.getElementById('aiQuantity').value;
    const difficulty = document.getElementById('aiDifficulty').value;
    const disciplineId = document.querySelector('input[name="disciplineId"]').value;

    if (!materialText.trim() && fileInput.files.length === 0) {
        alert("Por favor, cole um texto base OU envie um arquivo PDF.");
        return;
    }

    
    const btnGenerate = document.getElementById('btnGenerateAi');
    const loadingDiv = document.getElementById('aiLoading');
    const errorDiv = document.getElementById('aiError');
    
    btnGenerate.disabled = true;
    loadingDiv.classList.remove('d-none');
    errorDiv.classList.add('d-none');

    let fileBase64 = null;
    let fileMimeType = null;

    // Se o professor enviou um PDF, nós convertemos aqui
    if (fileInput.files.length > 0) {
        fileMimeType = fileInput.files[0].type;
        fileBase64 = await fileToBase64(fileInput.files[0]);
    }

    const requestData = {
        disciplineId: parseInt(disciplineId),
        material: materialText || "Material enviado via PDF em anexo.",
        questionType: type,
        quantity: parseInt(quantity),
        difficulty: difficulty,
        fileBase64: fileBase64,
        fileMimeType: fileMimeType
    };

    try {
        
        const response = await fetch('Ai?action=generate', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestData)
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.error || "Ocorreu um erro ao comunicar com a IA.");
        }

       
        const aiModalEl = document.getElementById('aiModal');
        const modal = bootstrap.Modal.getInstance(aiModalEl);
        modal.hide();

        
        renderGeneratedQuestions(data.questions);

    } catch (error) {
        errorDiv.textContent = error.message;
        errorDiv.classList.remove('d-none');
    } finally {
        btnGenerate.disabled = false;
        loadingDiv.classList.add('d-none');
    }
}


function renderGeneratedQuestions(questions) {
    const listContainer = document.getElementById('questions-list');
    
    questions.forEach((q) => {
        let items = listContainer.querySelectorAll('.question-item');
        let lastItem = items[items.length - 1];
        const statementField = lastItem?.querySelector('textarea[name="questionText"]');

        if (!lastItem || statementField.value.trim()) {
            document.getElementById('add-question').click();
            items = listContainer.querySelectorAll('.question-item');
            lastItem = items[items.length - 1];
        }

        
        const typeSelect = lastItem.querySelector('select[name="questionType"]');
        typeSelect.value = q.type;
        
        
        typeSelect.dispatchEvent(new Event('change'));

        lastItem.querySelector('textarea[name="questionText"]').value = q.statement;
        lastItem.querySelector('input[name="questionWeight"]').value = "1.0"; 

        
        if (q.type === 'ABERTA') {
            lastItem.querySelector('textarea[name="expectedAnswer"]').value = q.expectedAnswer;
            lastItem.querySelector('.open-question-fields').classList.remove('d-none');
            lastItem.querySelector('.closed-question-fields').classList.add('d-none');
        } else if (q.type === 'FECHADA' && q.alternatives) {
            lastItem.querySelector('input[name="optionA"]').value = q.alternatives["A"];
            lastItem.querySelector('input[name="optionB"]').value = q.alternatives["B"];
            lastItem.querySelector('input[name="optionC"]').value = q.alternatives["C"];
            lastItem.querySelector('input[name="optionD"]').value = q.alternatives["D"];
            lastItem.querySelector('select[name="correctOption"]').value = q.correctOption;
            
            lastItem.querySelector('.open-question-fields').classList.add('d-none');
            lastItem.querySelector('.closed-question-fields').classList.remove('d-none');
        }
    });
}
