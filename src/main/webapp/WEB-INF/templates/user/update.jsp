<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="custom-container mt-4">
    <div class="card border-0 shadow-sm p-4 mx-auto" style="max-width: 600px;">
        <h4 class="mb-4 text-center">Meu Perfil</h4>
        
        <form action="${pageContext.request.contextPath}/user/update-save" method="post" enctype="multipart/form-data">
            
            <input type="hidden" name="id" value="${usuarioLogado.id}">

            <!-- Área da Foto -->
            <div class="d-flex flex-column align-items-center mb-4">
                <img id="preview" src="${pageContext.request.contextPath}/assets/images/avatar/${not empty usuarioLogado.photo ? usuarioLogado.photo : 'default.png'}" 
                     class="rounded-circle mb-3" style="width: 150px; height: 150px; object-fit: cover; border: 3px solid #0056b3;">
                
                <label for="fotoInput" class="btn btn-outline-primary btn-sm">
                    Alterar Foto
                </label>
                <!-- O input real de arquivo fica escondido e é ativado pelo label acima -->
                <input type="file" id="fotoInput" name="foto" accept="image/*" class="d-none" onchange="previewImage(event)">
            </div>

            <div class="mb-3">
                <label class="form-label">Nome Completo</label>
                <input type="text" class="form-control" name="nome" value="${usuarioLogado.name}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">CPF</label>
                <input type="text" class="form-control" name="cpf" value="${usuarioLogado.cpf}" autocomplete="off" inputmode="numeric" maxlength="14" pattern="\d{3}\.\d{3}\.\d{3}-\d{2}" data-cpf-mask required>
            </div>
            <div class="mb-3">
                <label class="form-label">E-mail</label>
                <input type="text" class="form-control" name="email" value="${usuarioLogado.email}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Senha</label>
                <input type="text" class="form-control" name="senha">
            </div>
            <div class="d-flex justify-content-between mt-4">
                <div>
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">Cancelar</a>
                    <button type="submit" class="btn btn-primary">Salvar Alterações</button>
                </div>
                <a href="${pageContext.request.contextPath}/user/delete" 
                    class="btn btn-secondary" 
                    style="background-color: rgb(230, 73, 73);"
                    onclick="return confirm ('Confirmar exclusão, todas as informações relacionadas a esse perfil serão deletadas!');">
                    Excluir Conta
                </a>
            </div>
        </form>
    </div>
</div>

<script>
function previewImage(event) {
    var reader = new FileReader();
    reader.onload = function(){
        var output = document.getElementById('preview');
        output.src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
}
</script>
