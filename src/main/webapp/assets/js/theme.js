/**
 * TobIAs - Sistema de Educação
 * Theme JavaScript
 */

// Sidebar Toggle
document.addEventListener('DOMContentLoaded', function() {
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar = document.querySelector('.sidebar');
    const content = document.getElementById('content');
    const isDesktop = () => window.innerWidth > 992;

    if (sidebarToggle && sidebar && content) {
        sidebarToggle.addEventListener('click', function() {
            if (isDesktop()) {
                sidebar.classList.toggle('collapsed');
                content.classList.toggle('expanded');

                const isCollapsed = sidebar.classList.contains('collapsed');
                localStorage.setItem('sidebarCollapsed', isCollapsed);
                return;
            }

            sidebar.classList.toggle('mobile-open');
        });

        const sidebarCollapsed = localStorage.getItem('sidebarCollapsed') === 'true';
        if (sidebarCollapsed && isDesktop()) {
            sidebar.classList.add('collapsed');
            content.classList.add('expanded');
        }

        window.addEventListener('resize', function() {
            if (isDesktop()) {
                sidebar.classList.remove('mobile-open');
                return;
            }

            sidebar.classList.remove('collapsed');
            content.classList.remove('expanded');
        });
    }
});

// Active menu item
document.addEventListener('DOMContentLoaded', function() {
    const currentPath = window.location.pathname;
    const menuLinks = document.querySelectorAll('.sidebar-link');

    menuLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href && href !== '#' && currentPath.endsWith(href)) {
            link.classList.add('active');
        }
    });
});

// Notifications dropdown (placeholder)
document.addEventListener('DOMContentLoaded', function() {
    const notificationBtn = document.querySelector('.notification-btn');

    if (notificationBtn) {
        notificationBtn.addEventListener('click', function(e) {
            e.preventDefault();
            // TODO: Implement notifications dropdown
            console.log('Notifications clicked');
        });
    }
});

// Utility functions
const TobIAs = {
    // Show loading spinner
    showLoading: function(element) {
        if (element) {
            element.innerHTML = '<div class="spinner-border spinner-border-sm" role="status"><span class="visually-hidden">Loading...</span></div>';
        }
    },

    // Hide loading spinner
    hideLoading: function(element, originalContent) {
        if (element && originalContent) {
            element.innerHTML = originalContent;
        }
    },

    // Show toast notification
    showToast: function(message, type = 'info') {
        // Create toast container if it doesn't exist
        let toastContainer = document.querySelector('.toast-container');
        if (!toastContainer) {
            toastContainer = document.createElement('div');
            toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';
            toastContainer.style.zIndex = '9999';
            document.body.appendChild(toastContainer);
        }

        // Create toast
        const toast = document.createElement('div');
        toast.className = `toast align-items-center text-white bg-${type} border-0`;
        toast.setAttribute('role', 'alert');
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">${message}</div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;

        toastContainer.appendChild(toast);

        // Initialize and show toast
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();

        // Remove toast after it's hidden
        toast.addEventListener('hidden.bs.toast', function() {
            toast.remove();
        });
    },

    // Confirm dialog
    confirm: function(message, callback) {
        if (window.confirm(message)) {
            callback();
        }
    }
};

// Export for global use
window.TobIAs = TobIAs;
