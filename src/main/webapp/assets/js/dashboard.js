document.addEventListener('DOMContentLoaded', function() {
    const dashboardBlocks = document.querySelectorAll('.dashboard-welcome, .dashboard-stat-card, .dashboard-panel');

    if (!('IntersectionObserver' in window)) {
        return;
    }

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('is-visible');
                observer.unobserve(entry.target);
            }
        });
    }, {
        threshold: 0.1
    });

    dashboardBlocks.forEach(block => {
        block.classList.add('dashboard-reveal');
        observer.observe(block);
    });
});
