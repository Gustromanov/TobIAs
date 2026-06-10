<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tobias.dao.ActivityDAO" %>
<%@ page import="com.tobias.model.Activity" %>
<%@ page import="com.tobias.model.Discipline" %>
<%@ page import="com.tobias.model.Material" %>
<%@ page import="com.tobias.model.User" %>
<%@ page import="com.tobias.model.Warning" %>
<%@ page import="com.tobias.model.Comment" %>

<%!
    private static class TimelineItem {
        String type;
        String title;
        String description;
        String meta;
        String url;
        LocalDateTime date;

        TimelineItem(String type, String title, String description, String meta, String url, LocalDateTime date) {
            this.type = type;
            this.title = title;
            this.description = description;
            this.meta = meta;
            this.url = url;
            this.date = date;
        }
    }
%>

<%
    String tabParam = request.getParameter("tab");
    
    boolean showMuralTab = "mural".equals(tabParam) || tabParam == null || tabParam.isBlank();
    boolean showTimelineTab = "timeline".equals(tabParam);
    boolean showActivitiesTab = "atividades".equals(tabParam);
    boolean showMaterialsTab = "materiais".equals(tabParam);
    boolean showParticipantsTab = "participantes".equals(tabParam);

    Discipline discipline = (Discipline) request.getAttribute("discipline");
    User participant = (User) request.getAttribute("participant");
    List<User> students = (List<User>) request.getAttribute("students");
    List<Material> materials = (List<Material>) request.getAttribute("materials");
    List<Activity> activities = (List<Activity>) request.getAttribute("activities");
    List<Warning> warnings = (List<Warning>) request.getAttribute("warnings");

    boolean isProfessor = participant != null && participant.canManageDiscipline();
    boolean isStudent = participant != null && participant.canSubmitActivity();

    if (activities == null && request.getParameter("id") != null) {
        ActivityDAO activityDao = new ActivityDAO();
        activities = activityDao.listActivitiesByDiscipline(Integer.parseInt(request.getParameter("id")));
    }

    List<TimelineItem> timelineItems = new ArrayList<>();

    if (materials != null) {
        for (Material material : materials) {
            LocalDateTime date = material.getUploadedAt();
            String description = material.getContent() != null && !material.getContent().isBlank()
                    ? material.getContent()
                    : "Material de apoio publicado pelo professor.";
            String meta = material.getOriginalFileName() != null && !material.getOriginalFileName().isBlank()
                    ? material.getOriginalFileName()
                    : "Material";
            String url = material.hasFile()
                    ? request.getContextPath() + "/Material?action=download&id=" + material.getId()
                    : null;
            timelineItems.add(new TimelineItem("Material", material.getTitle(), description, meta, url, date));
        }
    }

    if (activities != null) {
        for (Activity activity : activities) {
            LocalDateTime date = activity.getSubmitDate() != null ? activity.getSubmitDate().atStartOfDay() : null;
            String meta = "Peso " + activity.getPeso();
            if (activity.getDeliveryDate() != null) {
                meta += " · Entrega em " + activity.getDeliveryDate();
            }
            timelineItems.add(new TimelineItem(
                    "Atividade",
                    activity.getTitle(),
                    "Atividade avaliativa adicionada pelo professor.",
                    meta,
                    request.getContextPath() + "/Activity?action=view&id=" + activity.getId(),
                    date));
        }
    }

    timelineItems.sort(Comparator.comparing((TimelineItem item) -> item.date == null ? LocalDateTime.MIN : item.date).reversed());
%>

<div class="custom-container mt-3">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="h3 mb-0 text-primary"><%= discipline != null ? discipline.getName() : "Disciplina" %></h2>
            <p class="text-muted mb-0">
                Código: <%= discipline != null ? discipline.getCode() : "" %>
                <% if (discipline != null && discipline.getProfessorName() != null) { %>
                    · Professor: <%= discipline.getProfessorName() %>
                <% } %>
                <% if (isProfessor) { %>
                    · Visão do professor
                <% } else if (isStudent) { %>
                    · Visão do aluno
                <% } %>
            </p>

            <% if (discipline != null && discipline.getDescription() != null && !discipline.getDescription().trim().isEmpty()) { %>
                <p class="text-secondary mt-2 mb-0" style="font-size: 0.95rem; max-width: 700px;">
                    <%= discipline.getDescription() %>
                </p>
            <% } %>
        </div>
   
        <a href="${pageContext.request.contextPath}/Disciplines" class="btn btn-action btn-action-back">
            <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                <path d="M10.8 5.4 4.2 12l6.6 6.6 1.4-1.4L8 13h12v-2H8l4.2-4.2-1.4-1.4Z"/>
            </svg>
            Voltar para Lista
        </a>
    </div>

    <ul class="nav nav-tabs mb-4" id="disciplineTabs" role="tablist">
        <li class="nav-item" role="presentation">
            <button class="nav-link <%= showMuralTab ? "active" : "" %>" id="mural-tab" data-bs-toggle="tab" data-bs-target="#mural" type="button" role="tab">
                 Avisos
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link <%= showTimelineTab ? "active" : "" %>" id="timeline-tab" data-bs-toggle="tab" data-bs-target="#timeline" type="button" role="tab">
                 Linha do tempo
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link <%= showMaterialsTab ? "active" : "" %>" id="materiais-tab" data-bs-toggle="tab" data-bs-target="#materiais" type="button" role="tab">
                 Materiais
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link <%= showActivitiesTab ? "active" : "" %>" id="atividades-tab" data-bs-toggle="tab" data-bs-target="#atividades" type="button" role="tab">
                 Atividades
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link <%= showParticipantsTab ? "active" : "" %>" id="participantes-tab" data-bs-toggle="tab" data-bs-target="#participantes" type="button" role="tab">
                 Participantes
            </button>
        </li>
    </ul>

    <div class="tab-content" id="disciplineTabsContent">
        <div class="tab-pane fade <%= showMuralTab ? "show active" : "" %>" id="mural" role="tabpanel">
            
            <% if (isProfessor) { %>
                <div class="card border-0 shadow-sm p-4 mb-4">
                    <div class="d-flex align-items-center gap-2 mb-3">
                        <h5 class="mb-0 text-primary">Escrever um aviso para a turma</h5>
                    </div>
                    <form action="${pageContext.request.contextPath}/Warning" method="post">
                        <input type="hidden" name="action" value="new">
                        <input type="hidden" name="disciplineId" value="<%= discipline != null ? discipline.getId() : 0 %>">
                        <div class="mb-3">
                            <textarea class="form-control" name="content" rows="3" placeholder="Avise a turma sobre uma prova, trabalho ou partilhe um recado..." required></textarea>
                        </div>
                        <div class="text-end">
                            <button type="submit" class="btn btn-sm btn-primary" style="background-color: #6f42c1; border-color: #6f42c1;">Postar Aviso</button>
                        </div>
                    </form>
                </div>
            <% } %>

            <% if (warnings != null && !warnings.isEmpty()) { %>
                <% for (Warning warning : warnings) { %>
                    <div class="card border-0 shadow-sm p-4 mb-4">
                        <div class="d-flex align-items-center gap-3 mb-3">
                            <img src="${pageContext.request.contextPath}/assets/images/avatar/<%= warning.getAuthorPhoto() != null ? warning.getAuthorPhoto() : "default.png" %>" class="rounded-circle" style="width: 45px; height: 45px; object-fit: cover; border: 2px solid #6f42c1;">
                            <div>
                                <strong class="d-block text-dark"><%= warning.getAuthorName() %></strong>
                                <small class="text-muted">Publicado em <%= warning.getPublishedAt().toLocalDate() %></small>
                            </div>
                        </div>
                        
                        <p class="text-dark fs-6" style="white-space: pre-wrap;"><%= warning.getContent() %></p>
                        <hr class="text-muted my-3">

                        <div class="ps-2 border-start" style="border-width: 3px !important; border-color: #e9ecef !important;">
                            <h6 class="text-secondary mb-3 small">Comentários da turma</h6>
                            
                            <% if (warning.getComments() != null && !warning.getComments().isEmpty()) { %>
                                <% for (Comment comment : warning.getComments()) { %>
                                    <div class="d-flex align-items-start gap-2 mb-3">
                                        <img src="${pageContext.request.contextPath}/assets/images/avatar/<%= comment.getAuthorPhoto() != null ? comment.getAuthorPhoto() : "default.png" %>" class="rounded-circle" style="width: 32px; height: 32px; object-fit: cover;">
                                        <div class="bg-light p-2 rounded flex-grow-1">
                                            <strong class="d-block small text-dark"><%= comment.getAuthorName() %></strong>
                                            <span class="small text-muted"><%= comment.getContent() %></span>
                                        </div>
                                    </div>
                                <% } %>
                            <% } %>

                            <form action="${pageContext.request.contextPath}/Warning" method="post" class="mt-3">
                                <input type="hidden" name="action" value="new-comment">
                                <input type="hidden" name="warningId" value="<%= warning.getId() %>">
                                <input type="hidden" name="disciplineId" value="<%= discipline != null ? discipline.getId() : 0 %>">
                                <div class="input-group input-group-sm">
                                    <input type="text" class="form-control" name="content" placeholder="Escreva um comentário para a turma..." required>
                                    <button class="btn btn-outline-primary" type="submit" style="color: #6f42c1; border-color: #e9ecef;">Responder</button>
                                </div>
                            </form>
                        </div>
                    </div>
                <% } %>
            <% } else { %>
                <div class="text-center py-5 text-muted">
                    <p class="mb-0">O mural está limpo. Nenhum aviso publicado por aqui ainda!</p>
                </div>
            <% } %>
        </div>

        <div class="tab-pane fade <%= showTimelineTab ? "show active" : "" %>" id="timeline" role="tabpanel">
            <div class="activity-panel">
                <div class="activity-panel-header">
                    <div>
                        <h3 class="activity-panel-title">Linha do tempo da disciplina</h3>
                        <p class="activity-subtitle">Tudo que o professor adicionou aparece aqui em ordem de publicação.</p>
                    </div>
                    <% if (isProfessor) { %>
                        <div class="activity-actions">
                            <a href="${pageContext.request.contextPath}/Material?action=new&disciplineId=<%= discipline != null ? discipline.getId() : 0 %>" class="btn btn-sm btn-action btn-action-add">Novo material</a>
                            <a href="${pageContext.request.contextPath}/Activity?action=new&disciplineId=${param.id}" class="btn btn-sm btn-action btn-action-save">Nova atividade</a>
                        </div>
                    <% } %>
                </div>

                <% if (!timelineItems.isEmpty()) { %>
                    <div class="discipline-timeline">
                        <% for (TimelineItem item : timelineItems) { %>
                            <div class="timeline-entry">
                                <div class="timeline-marker <%= "Atividade".equals(item.type) ? "timeline-marker-activity" : "timeline-marker-material" %>">
                                    <i class="ti <%= "Atividade".equals(item.type) ? "ti-clipboard-check" : "ti-file-text" %>"></i>
                                </div>
                                <div class="timeline-card">
                                    <div class="timeline-card-header">
                                        <span class="timeline-type"><%= item.type %></span>
                                        <% if (item.date != null) { %>
                                            <span class="timeline-date"><%= item.date.toLocalDate() %></span>
                                        <% } %>
                                    </div>
                                    <h4><%= item.title != null ? item.title : item.type %></h4>
                                    <p><%= item.description %></p>
                                    <div class="timeline-footer">
                                        <span><%= item.meta %></span>
                                        <% if (item.url != null) { %>
                                            <a href="<%= item.url %>">Abrir</a>
                                        <% } %>
                                    </div>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } else { %>
                    <div class="empty-state">Nada foi adicionado pelo professor ainda.</div>
                <% } %>
            </div>
        </div>

        <div class="tab-pane fade <%= showMaterialsTab ? "show active" : "" %>" id="materiais" role="tabpanel">
            <div class="card border-0 shadow-sm p-4">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="mb-0">Materiais de Apoio</h5>
                    <% if (isProfessor) { %>
                         <a href="${pageContext.request.contextPath}/Material?action=new&disciplineId=<%= discipline != null ? discipline.getId() : 0 %>" class="btn btn-sm btnadd text-white">+ Novo Material</a>
                    <% } %>
                </div>
                <hr>
                <% if (materials != null && !materials.isEmpty()) { %>
                    <div class="list-group list-group-flush">
                        <% for (Material material : materials) { %>
                            <div class="list-group-item px-0">
                                <div class="d-flex justify-content-between align-items-start gap-3">
                                    <div class="flex-grow-1">
                                        <h6 class="mb-1"><%= material.getTitle() != null ? material.getTitle() : "Material sem título" %></h6>
                                        <% if (material.getContent() != null && !material.getContent().isBlank()) { %>
                                            <p class="text-muted mb-2"><%= material.getContent() %></p>
                                        <% } %>
                                        <small class="text-muted">
                                            <% if (material.getUploadedAt() != null) { %>
                                                Publicado em <%= material.getUploadedAt().toLocalDate() %>
                                            <% } %>
                                            <% if (material.getOriginalFileName() != null && !material.getOriginalFileName().isBlank()) { %>
                                                · <%= material.getOriginalFileName() %>
                                            <% } %>
                                        </small>
                                    </div>
                                    <div class="activity-actions justify-content-end">
                                         <% if (material.hasFile()) { %>
                                            <a href="${pageContext.request.contextPath}/Material?action=download&id=<%= material.getId() %>" class="btn btn-sm btn-action material-action-icon btn-action-save" title="Baixar material" aria-label="Baixar material">
                                                 <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                                                    <path d="M11 4h2v9.2l3.1-3.1 1.4 1.4L12 17l-5.5-5.5 1.4-1.4 3.1 3.1V4Zm-6 15h14v2H5v-2Z"/>
                                                 </svg>
                                            </a>
                                         <% } %>
                                        <% if (isProfessor) { %>
                                             <a href="${pageContext.request.contextPath}/Material?action=edit&id=<%= material.getId() %>" class="btn btn-sm btn-action material-action-icon btn-action-edit" title="Editar material" aria-label="Editar material">
                                                <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                                                    <path d="M17.7 3.3a1 1 0 0 1 1.4 0l1.6 1.6a1 1 0 0 1 0 1.4L8.9 18.1 4 19.5l1.4-4.9L17.7 3.3Zm-10.5 12-.5 1.9 1.9-.5L16.6 8.7l-1.4-1.4-8 8ZM17.9 7.3 18.6 6 18 5.4l-1.3.7 1.2 1.2Z"/>
                                                 </svg>
                                            </a>
                                             <form action="${pageContext.request.contextPath}/Material" method="post">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="materialId" value="<%= material.getId() %>">
                                                <button type="submit" class="btn btn-sm btn-action material-action-icon btn-action-delete" title="Excluir material" aria-label="Excluir material">
                                                     <svg class="ui-icon" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                                                        <path d="M9 3h6l1 2h4v2H4V5h4l1-2Zm-2 6h10l-.7 12H7.7L7 9Zm2.1 2 .5 8h1.8l-.4-8H9.1Zm3.9 0v8h2v-8h-2Z"/>
                                                     </svg>
                                                </button>
                                             </form>
                                        <% } %>
                                    </div>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } else { %>
                    <p class="text-muted text-center py-4">Nenhum material disponibilizado pelo professor ainda.</p>
                <% } %>
            </div>
        </div>

        <div class="tab-pane fade <%= showActivitiesTab ? "show active" : "" %>" id="atividades" role="tabpanel">
            <div class="card border-0 shadow-sm p-4">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="mb-0">Atividades Avaliativas</h5>
                    <% if (isProfessor) { %>
                         <a href="${pageContext.request.contextPath}/Activity?action=new&disciplineId=${param.id}" class="btn btn-sm btnadd text-white">+ Nova Atividade</a>
                    <% } %>
                </div>
                <hr>
                <% if (activities != null && !activities.isEmpty()) { %>
                    <div class="list-group list-group-flush">
                        <% for (Activity activity : activities) { %>
                            <div class="list-group-item px-0">
                                <div class="d-flex justify-content-between align-items-start gap-3">
                                     <div>
                                        <a href="${pageContext.request.contextPath}/Activity?action=view&id=<%= activity.getId() %>" class="text-decoration-none">
                                            <h6 class="mb-1"><%= activity.getTitle() %></h6>
                                        </a>
                                        <small class="text-muted">
                                             Atribuída em <%= activity.getSubmitDate() %>
                                            <% if (activity.getDeliveryDate() != null) { %>
                                               · Entrega em <%= activity.getDeliveryDate() %>
                                            <% } %>
                                      </small>
                                    </div>
                                    <span class="badge bg-primary rounded-pill">Peso <%= activity.getPeso() %></span>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <%
                    } else {
                %>
                    <p class="text-muted text-center py-4">Nenhuma atividade no momento. Pode descansar!</p>
                <%
                    }
                %>
            </div>
        </div>

        <div class="tab-pane fade <%= showParticipantsTab ? "show active" : "" %>" id="participantes" role="tabpanel">
            <div class="card border-0 shadow-sm p-4">
                <h5 class="text-primary mb-3">Professores</h5>
                <ul class="list-group list-group-flush mb-4">
                    <li class="list-group-item d-flex align-items-center gap-3">
                        <div class="bg-primary text-white rounded-circle d-flex justify-content-center align-items-center" style="width: 40px; height: 40px;">
                             <strong>P</strong>
                        </div>
                        <span><%= discipline != null && discipline.getProfessorName() != null ? discipline.getProfessorName() : "Professor Responsável" %></span>
                    </li>
                </ul>

                <h5 class="text-primary mb-3">Colegas de Turma</h5>
                <% if (students != null && !students.isEmpty()) { %>
                     <ul class="list-group list-group-flush">
                        <% for (User student : students) { %>
                            <li class="list-group-item d-flex justify-content-between align-items-center gap-3">
                                <div class="d-flex align-items-center gap-3">
                                    <div class="bg-secondary text-white rounded-circle d-flex justify-content-center align-items-center" style="width: 40px; height: 40px;">
                                        <strong><%= student.getName() != null && !student.getName().isBlank() ? student.getName().substring(0, 1).toUpperCase() : "A" %></strong>
                                    </div>
                                    <div>
                                        <div><%= student.getName() %></div>
                                        <small class="text-muted"><%= student.getEmail() %></small>
                                    </div>
                                </div>
                                <span class="badge bg-secondary">Aluno</span>
                            </li>
                         <% } %>
                    </ul>
                <% } else { %>
                    <p class="text-muted px-3">Nenhum aluno entrou nessa disciplina ainda.</p>
                <% } %>
            </div>
        </div>

    </div>
</div>
