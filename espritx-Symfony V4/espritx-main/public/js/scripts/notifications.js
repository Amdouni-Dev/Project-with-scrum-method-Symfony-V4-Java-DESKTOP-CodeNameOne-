$(document).ready(function (e) {
    $("span.unread-notifications-count").innerText = notifications.unread_count;
    $("a.anchor-async-seen").click(function (e) {
        e.stopPropagation();
        const parent = e.currentTarget.parentElement;
        const current = $(this)[0];
        $.get($(this).data("target"), function (data) {
            notifications.unread_count = notifications.unread_count - 1;
            console.log("Marked as seen.")
            parent.querySelector("a.anchor-async-unseen").classList.remove("hidden")
            current.classList.add("hidden")
        });
    });

    $("a.anchor-async-unseen").click(function (e) {
        e.stopPropagation();
        const parent = e.currentTarget.parentElement;
        const current = $(this)[0];
        $.get($(this).data("target"), function (data) {
            notifications.unread_count = notifications.unread_count + 1;
            console.debug("Marked as unseen.")
            parent.querySelector("a.anchor-async-seen").classList.remove("hidden")
            current.classList.add("hidden")
        });
    });

    $(".notification-row")
        .hoverIntent(function (e) {
            let seen_anchor = $(this).find("a.anchor-async-seen")[0];
            if (seen_anchor && isVisible(seen_anchor)) {
                $(this).children("a.anchor-async-seen").simulate("click")
            }
        })
        .click(function (e) {
            window.location.href = $(this)[0].dataset.navigation;
        });

    $(".read-all-notifications-anchor").click(function (e) {
        e.preventDefault();
        $.get($(this).data("target"), function (data) {
            console.log("All notifications marked as read.")
        });
        let notifications_pane = e.currentTarget.parentElement.previousElementSibling;
        notifications.unread_count = 0;
        notifications_pane.querySelector("a.anchor-async-seen").classList.add("hidden");
        notifications_pane.querySelector("a.anchor-async-unseen").classList.remove("hidden");
    });
});

function isVisible(e) {
    return !!(e.offsetWidth || e.offsetHeight || e.getClientRects().length);
}