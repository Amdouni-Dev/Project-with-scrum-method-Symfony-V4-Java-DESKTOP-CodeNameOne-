window.colors = {
    solid: {
        primary: "#7367F0",
        secondary: "#82868b",
        success: "#28C76F",
        info: "#00cfe8",
        warning: "#FF9F43",
        danger: "#EA5455",
        dark: "#4b4b4b",
        black: "#000",
        white: "#fff",
        body: "#f8f8f8"
    },
    light: {
        primary: "#7367F01a",
        secondary: "#82868b1a",
        success: "#28C76F1a",
        info: "#00cfe81a",
        warning: "#FF9F431a",
        danger: "#EA54551a",
        dark: "#4b4b4b1a"
    }
}, function (a, e, t) {
    "use strict";
    var s = t("html"), n = t("body"), o = "#4e5154", l = "../../../app-assets/";
    if ("laravel" === t("body").attr("data-framework") && (l = t("body").attr("data-asset-path")), t.fn.dataTable && t.extend(t.fn.dataTable.ext.classes, {
        sFilterInput: "form-control",
        sLengthSelect: "form-select"
    }), t(a).on("load", (function () {
        var l = !1;
        (n.hasClass("menu-collapsed") || "true" === localStorage.getItem("menuCollapsed")) && (l = !0), t("html").data("textdirection"), setTimeout((function () {
            s.removeClass("loading").addClass("loaded")
        }), 1200), t.app.menu.init(l), !1 === t.app.nav.initialized && t.app.nav.init({speed: 300}), Unison.on("change", (function (a) {
            t.app.menu.change(l)
        })), [].slice.call(e.querySelectorAll('[data-bs-toggle="tooltip"]')).map((function (a) {
            return new bootstrap.Tooltip(a)
        })), t('a[data-action="collapse"]').on("click", (function (a) {
            a.preventDefault(), t(this).closest(".card").children(".card-content").collapse("toggle"), t(this).closest(".card").find('[data-action="collapse"]').toggleClass("rotate")
        })), t(".touchspin-cart").length > 0 && t(".touchspin-cart").TouchSpin({
            buttondown_class: "btn btn-primary",
            buttonup_class: "btn btn-primary",
            buttondown_txt: feather.icons.minus.toSvg(),
            buttonup_txt: feather.icons.plus.toSvg()
        }), t(".dropdown-notification .dropdown-menu, .dropdown-cart .dropdown-menu").on("click", (function (a) {
            a.stopPropagation()
        })), t(".scrollable-container").each((function () {
            new PerfectScrollbar(t(this)[0], {wheelPropagation: !1})
        })), t('a[data-action="reload"]').on("click", (function () {
            var a = t(this).closest(".card");
            if (s.hasClass("dark-layout")) var e = "#10163a"; else e = "#fff";
            a.block({
                message: feather.icons["refresh-cw"].toSvg({class: "font-medium-1 spinner text-primary"}),
                timeout: 2e3,
                overlayCSS: {backgroundColor: e, cursor: "wait"},
                css: {border: 0, padding: 0, backgroundColor: "none"}
            })
        })), t('a[data-action="close"]').on("click", (function () {
            t(this).closest(".card").removeClass().slideUp("fast")
        })), t('.card .heading-elements a[data-action="collapse"]').on("click", (function () {
            var a, e = t(this).closest(".card");
            parseInt(e[0].style.height, 10) > 0 ? (a = e.css("height"), e.css("height", "").attr("data-height", a)) : e.data("height") && (a = e.data("height"), e.css("height", a).attr("data-height", ""))
        })), t("input:disabled, textarea:disabled").closest(".input-group").addClass("disabled"), t(".main-menu-content").find("li.active").parents("li").addClass("sidebar-group-active");
        var r = n.data("menu");
        "horizontal-menu" != r && !1 === l && t(".main-menu-content").find("li.active").parents("li").addClass("open"), "horizontal-menu" == r && (t(".main-menu-content").find("li.active").parents("li:not(.nav-item)").addClass("open"), t(".main-menu-content").find("li.active").closest("li.nav-item").addClass("sidebar-group-active open"));
        var i = t(".chartjs"), c = i.children("canvas").attr("height"), d = t(".main-menu");
        if (i.css("height", c), n.hasClass("boxed-layout") && n.hasClass("vertical-overlay-menu")) {
            var h = d.width(), u = t(".app-content").position().left - h;
            n.hasClass("menu-flipped") ? d.css("right", u + "px") : d.css("left", u + "px")
        }
        t(".char-textarea").on("keyup", (function (e) {
            var s, n, l, r, i;
            s = this, n = e, l = parseInt(t(s).data("length")), r = t(".textarea-counter-value"), i = t(".char-textarea"), function (a) {
                return 8 == a.keyCode || 46 == a.keyCode || 37 == a.keyCode || 38 == a.keyCode || 39 == a.keyCode || 40 == a.keyCode
            }(n) || s.value.length < l - 1 && (s.value = s.value.substring(0, l)), t(".char-count").html(s.value.length), s.value.length > l ? (r.css("background-color", a.colors.solid.danger), i.css("color", a.colors.solid.danger), i.addClass("max-limit")) : (r.css("background-color", a.colors.solid.primary), i.css("color", o), i.removeClass("max-limit")), t(this).addClass("active")
        })), t(".content-overlay").on("click", (function () {
            t(".search-list").removeClass("show");
            var a = t(".search-input-close").closest(".search-input");
            a.hasClass("open") && (a.removeClass("open"), y.val(""), y.blur(), w.removeClass("show")), t(".app-content").removeClass("show-overlay"), t(".bookmark-wrapper .bookmark-input").removeClass("show")
        }));
        var m = e.getElementsByClassName("main-menu-content");
        m.length > 0 && m[0].addEventListener("ps-scroll-y", (function () {
            t(this).find(".ps__thumb-y").position().top > 0 ? t(".shadow-bottom").css("display", "block") : t(".shadow-bottom").css("display", "none")
        }))
    })), t(e).on("click", ".sidenav-overlay", (function (a) {
        return t.app.menu.hide(), !1
    })), "undefined" != typeof Hammer) {
        var r;
        "rtl" == t("html").data("textdirection") && (r = !0);
        var i = e.querySelector(".drag-target"), c = "panright", d = "panleft";
        !0 === r && (c = "panleft", d = "panright"), t(i).length > 0 && new Hammer(i).on(c, (function (a) {
            if (n.hasClass("vertical-overlay-menu")) return t.app.menu.open(), !1
        })), setTimeout((function () {
            var a, s = e.querySelector(".main-menu");
            t(s).length > 0 && ((a = new Hammer(s)).get("pan").set({
                direction: Hammer.DIRECTION_ALL,
                threshold: 250
            }), a.on(d, (function (a) {
                if (n.hasClass("vertical-overlay-menu")) return t.app.menu.hide(), !1
            })))
        }), 300);
        var h = e.querySelector(".sidenav-overlay");
        t(h).length > 0 && new Hammer(h).on("tap", (function (a) {
            if (n.hasClass("vertical-overlay-menu")) return t.app.menu.hide(), !1
        }))
    }
    if (t(e).on("click", ".menu-toggle, .modern-nav-toggle", (function (e) {
        return e.preventDefault(), t.app.menu.toggle(), setTimeout((function () {
            t(a).trigger("resize")
        }), 200), t("#collapse-sidebar-switch").length > 0 && setTimeout((function () {
            n.hasClass("menu-expanded") || n.hasClass("menu-open") ? t("#collapse-sidebar-switch").prop("checked", !1) : t("#collapse-sidebar-switch").prop("checked", !0)
        }), 50), n.hasClass("menu-expanded") || n.hasClass("menu-open") ? localStorage.setItem("menuCollapsed", !1) : localStorage.setItem("menuCollapsed", !0), !1
    })), t(".navigation").find("li").has("ul").addClass("has-sub"), t(a).resize((function () {
        t.app.menu.manualScroller.updateHeight()
    })), t("#sidebar-page-navigation").on("click", "a.nav-link", (function (a) {
        a.preventDefault(), a.stopPropagation();
        var e = t(this), s = e.attr("href"), n = t(s).offset().top - 80;
        t("html, body").animate({scrollTop: n}, 0), setTimeout((function () {
            e.parent(".nav-item").siblings(".nav-item").children(".nav-link").removeClass("active"), e.addClass("active")
        }), 100)
    })), "laravel" === n.attr("data-framework")) {
        /* todo: fix me?
        var u = t("html")[0].lang;
        if (null !== u) {
            var m = t(".dropdown-language").find("a[data-language=" + u + "]").text(),
                f = t(".dropdown-language").find("a[data-language=" + u + "] .flag-icon").attr("class");
            t("#dropdown-flag .selected-language").text(m), t("#dropdown-flag .flag-icon").removeClass().addClass(f)
        }*/
    } else i18next.use(a.i18nextXHRBackend).init({
        debug: !1,
        fallbackLng: "en",
        backend: {loadPath: l + "data/locales/{{lng}}.json"},
        returnObjects: !0
    }, (function (a, e) {
        jqueryI18next.init(i18next, t)
    })), t(".dropdown-language .dropdown-item").on("click", (function () {
        var a = t(this);
        a.siblings(".selected").removeClass("selected"), a.addClass("selected");
        var e = a.text(), s = a.find(".flag-icon").attr("class");
        t("#dropdown-flag .selected-language").text(e), t("#dropdown-flag .flag-icon").removeClass().addClass(s);
        var n = a.data("language");
        i18next.changeLanguage(n, (function (a, e) {
            t(".main-menu, .horizontal-menu-wrapper").localize()
        }))
    }));
    var p = t(".search-input input").data("search"), v = t(".bookmark-wrapper"),
        g = t(".bookmark-wrapper .bookmark-star"), b = t(".bookmark-wrapper .bookmark-input"),
        C = t(".nav-link-search"), k = t(".search-input"), y = t(".search-input input"),
        w = t(".search-input .search-list"), x = t(".app-content"), S = t(".bookmark-input .search-list");
    if (g.on("click", (function (a) {
        a.stopPropagation(), b.toggleClass("show"), b.find("input").val(""), b.find("input").blur(), b.find("input").focus(), v.find(".search-list").addClass("show");
        var e = t("ul.nav.navbar-nav.bookmark-icons li"), s = "", n = "";
        t("ul.search-list li").remove();
        for (var o = 0; o < e.length; o++) {
            n = 0 === o ? "current_item" : "";
            var l = "", r = "";
            if (t(e[o].firstChild.firstChild).hasClass("feather")) {
                var i = e[o].firstChild.firstChild.getAttribute("class");
                l = i.split("feather-")[1].split(" ")[0], r = i.split("feather-")[1].split(" ")[1]
            }
            s += '<li class="auto-suggestion ' + n + '"><a class="d-flex align-items-center justify-content-between w-100" href=' + e[o].firstChild.href + '><div class="d-flex justify-content-start align-items-center">' + feather.icons[l].toSvg({class: "me-75 " + r}) + "<span>" + e[o].firstChild.dataset.bsOriginalTitle + "</span></div>" + feather.icons.star.toSvg({class: "text-warning bookmark-icon float-end"}) + "</a></li>"
        }
        t("ul.search-list").append(s)
    })), C.on("click", (function () {
        t(this), t(this).parent(".nav-search").find(".search-input").addClass("open"), y.focus(), w.find("li").remove(), b.removeClass("show")
    })), t(".search-input-close").on("click", (function () {
        t(this);
        var a = t(this).closest(".search-input");
        a.hasClass("open") && (a.removeClass("open"), y.val(""), y.blur(), w.removeClass("show"), x.removeClass("show-overlay"))
    })), t(".search-list-main").length) var I = new PerfectScrollbar(".search-list-main", {wheelPropagation: !1});

    function z() {
        return s.hasClass("dark-layout") ? "dark-layout" : s.hasClass("bordered-layout") ? "bordered-layout" : s.hasClass("semi-dark-layout") ? "semi-dark-layout" : "light-layout"
    }

    t(".search-list-bookmark").length && new PerfectScrollbar(".search-list-bookmark", {wheelPropagation: !1}), t(".search-list-main").mouseenter((function () {
        I.update()
    })), y.on("keyup", (function (a) {
        if (t(this).closest(".search-list").addClass("show"), 38 !== a.keyCode && 40 !== a.keyCode && 13 !== a.keyCode) {
            27 == a.keyCode && (x.removeClass("show-overlay"), b.find("input").val(""), b.find("input").blur(), y.val(""), y.blur(), k.removeClass("open"), k.hasClass("show") && (t(this).removeClass("show"), k.removeClass("show")));
            var e = t(this).val().toLowerCase(), s = "", n = !1;
            if (t("ul.search-list li").remove(), t(this).parent().hasClass("bookmark-input") && (n = !0), "" != e) {
                x.addClass("show-overlay"), b.focus() ? S.addClass("show") : (w.addClass("show"), S.removeClass("show")), !1 === n && (w.addClass("show"), S.removeClass("show"));
                var o = "", r = "", i = "", c = "",
                    d = '<li class="d-flex align-items-center"><a href="#"><h6 class="section-label mt-75 mb-0">Pages</h6></a></li>',
                    h = "", u = "", m = 0;
                t.getJSON(l + "data/" + p + ".json", (function (a) {
                    for (var f = 0; f < a.listItems.length; f++) {
                        if ("laravel" === t("body").attr("data-framework") && (a.listItems[f].url = l + a.listItems[f].url), !0 === n) {
                            s = "";
                            for (var p = t("ul.nav.navbar-nav.bookmark-icons li"), v = 0; v < p.length; v++) {
                                if (a.listItems[f].name === p[v].firstChild.dataset.bsOriginalTitle) {
                                    s = " text-warning";
                                    break
                                }
                                s = ""
                            }
                            h = feather.icons.star.toSvg({class: "bookmark-icon float-end" + s})
                        }
                        0 == a.listItems[f].name.toLowerCase().indexOf(e) && m < 5 && (o += '<li class="auto-suggestion ' + (0 === m ? "current_item" : "") + '"><a class="d-flex align-items-center justify-content-between w-100" href=' + a.listItems[f].url + '><div class="d-flex justify-content-start align-items-center">' + feather.icons[a.listItems[f].icon].toSvg({class: "me-75 "}) + "<span>" + a.listItems[f].name + "</span></div>" + h + "</a></li>", m++)
                    }
                    for (f = 0; f < a.listItems.length; f++) {
                        if (!0 === n) {
                            for (s = "", p = t("ul.nav.navbar-nav.bookmark-icons li"), v = 0; v < p.length; v++) s = a.listItems[f].name === p[v].firstChild.dataset.bsOriginalTitle ? " text-warning" : "";
                            h = feather.icons.star.toSvg({class: "bookmark-icon float-end" + s})
                        }
                        0 != a.listItems[f].name.toLowerCase().indexOf(e) && a.listItems[f].name.toLowerCase().indexOf(e) > -1 && m < 5 && (r += '<li class="auto-suggestion ' + (0 === m ? "current_item" : "") + '"><a class="d-flex align-items-center justify-content-between w-100" href=' + a.listItems[f].url + '><div class="d-flex justify-content-start align-items-center">' + feather.icons[a.listItems[f].icon].toSvg({class: "me-75 "}) + "<span>" + a.listItems[f].name + "</span></div>" + h + "</a></li>", m++)
                    }
                    u = t(".main-search-list-defaultlist").html(), "" == o && "" == r && (r = t(".main-search-list-defaultlist-other-list").html()), i = d.concat(o, r, u), t("ul.search-list").html(i), c = o.concat(r), t("ul.search-list-bookmark").html(c)
                }))
            } else if (!0 === n) {
                for (var f = t("ul.nav.navbar-nav.bookmark-icons li"), v = "", g = 0; g < f.length; g++) {
                    var C = "";
                    if (t(f[g].firstChild.firstChild).hasClass("feather")) {
                        var I = f[g].firstChild.firstChild.getAttribute("class");
                        C = I.split("feather-")[1].split(" ")[0], I.split("feather-")[1].split(" ")[1]
                    }
                    v += '<li class="auto-suggestion"><a class="d-flex align-items-center justify-content-between w-100" href=' + f[g].firstChild.href + '><div class="d-flex justify-content-start align-items-center">' + feather.icons[C].toSvg({class: "me-75 "}) + "<span>" + f[g].firstChild.dataset.bsOriginalTitle + "</span></div>" + feather.icons.star.toSvg({class: "text-warning bookmark-icon float-end"}) + "</a></li>"
                }
                t("ul.search-list").append(v)
            } else x.hasClass("show-overlay") && x.removeClass("show-overlay"), w.hasClass("show") && w.removeClass("show")
        }
    })), t(e).on("mouseenter", ".search-list li", (function (a) {
        t(this).siblings().removeClass("current_item"), t(this).addClass("current_item")
    })), t(e).on("click", ".search-list li", (function (a) {
        a.stopPropagation()
    })), t("html").on("click", (function (a) {
        t(a.target).hasClass("bookmark-icon") || (S.hasClass("show") && S.removeClass("show"), b.hasClass("show") && (b.removeClass("show"), x.removeClass("show-overlay")))
    })), t(e).on("click", ".bookmark-input input", (function (a) {
        b.addClass("show"), S.addClass("show")
    })), t(e).on("click", ".bookmark-input .search-list .bookmark-icon", (function (a) {
        if (a.stopPropagation(), t(this).hasClass("text-warning")) {
            t(this).removeClass("text-warning");
            for (var e = t("ul.nav.navbar-nav.bookmark-icons li"), s = 0; s < e.length; s++) e[s].firstChild.dataset.bsOriginalTitle == t(this).parent()[0].innerText && e[s].remove();
            a.preventDefault()
        } else {
            e = t("ul.nav.navbar-nav.bookmark-icons li"), t(this).addClass("text-warning"), a.preventDefault();
            var n, o = t(this).parent()[0].href, l = t(this).parent()[0].innerText,
                r = t(this).parent()[0].firstChild.firstChild.dataset.icon;
            t(t(this).parent()[0].firstChild.firstChild).hasClass("feather") && (r = t(this).parent()[0].firstChild.firstChild.getAttribute("class").split("feather-")[1].split(" ")[0]), n = '<li class="nav-item d-none d-lg-block"><a class="nav-link" href="' + o + '" data-bs-toggle="tooltip" data-bs-placement="bottom" title="' + l + '">' + feather.icons[r].toSvg({class: "ficon"}) + "</a></li>", t("ul.nav.bookmark-icons").append(n), t('[data-bs-toggle="tooltip"]').tooltip()
        }
    })), t(a).on("keydown", (function (e) {
        var s, n, o = t(".search-list li.current_item");
        if (40 === e.keyCode ? (s = o.next(), o.removeClass("current_item"), o = s.addClass("current_item")) : 38 === e.keyCode && (n = o.prev(), o.removeClass("current_item"), o = n.addClass("current_item")), 13 === e.keyCode && t(".search-list li.current_item").length > 0) {
            var l = t(".search-list li.current_item a");
            a.location = l.attr("href"), t(l).trigger("click")
        }
    })), Waves.init(), Waves.attach(".btn:not([class*='btn-relief-']):not([class*='btn-gradient-']):not([class*='btn-outline-']):not([class*='btn-flat-'])", ["waves-float", "waves-light"]), Waves.attach("[class*='btn-outline-']"), Waves.attach("[class*='btn-flat-']"), t(".form-password-toggle .input-group-text").on("click", (function (a) {
        a.preventDefault();
        var e = t(this), s = e.closest(".form-password-toggle"), n = e, o = s.find("input");
        "text" === o.attr("type") ? (o.attr("type", "password"), feather && n.find("svg").replaceWith(feather.icons.eye.toSvg({class: "font-small-4"}))) : "password" === o.attr("type") && (o.attr("type", "text"), feather && n.find("svg").replaceWith(feather.icons["eye-off"].toSvg({class: "font-small-4"})))
    })), t(a).on("scroll", (function () {
        t(this).scrollTop() > 400 ? t(".scroll-top").fadeIn() : t(".scroll-top").fadeOut(), n.hasClass("navbar-static") && (t(a).scrollTop() > 65 ? (t("html:not(.dark-layout) .horizontal-menu .header-navbar.navbar-fixed").css({
            background: "#fff",
            "box-shadow": "0 4px 20px 0 rgba(0,0,0,.05)"
        }), t(".horizontal-menu.dark-layout .header-navbar.navbar-fixed").css({
            background: "#161d31",
            "box-shadow": "0 4px 20px 0 rgba(0,0,0,.05)"
        }), t("html:not(.dark-layout) .horizontal-menu .horizontal-menu-wrapper.header-navbar").css("background", "#fff"), t(".dark-layout .horizontal-menu .horizontal-menu-wrapper.header-navbar").css("background", "#161d31")) : (t("html:not(.dark-layout) .horizontal-menu .header-navbar.navbar-fixed").css({
            background: "#f8f8f8",
            "box-shadow": "none"
        }), t(".dark-layout .horizontal-menu .header-navbar.navbar-fixed").css({
            background: "#161d31",
            "box-shadow": "none"
        }), t("html:not(.dark-layout) .horizontal-menu .horizontal-menu-wrapper.header-navbar").css("background", "#fff"), t(".dark-layout .horizontal-menu .horizontal-menu-wrapper.header-navbar").css("background", "#161d31")))
    })), t(".scroll-top").on("click", (function () {
        t("html, body").animate({scrollTop: 0}, 75)
    }));
    var T = s.attr("data-layout") ? s.attr("data-layout") : "light-layout";
    t(".nav-link-style").on("click", (function () {
        var a = z(), e = "", n = localStorage.getItem(T + "-prev-skin", a);
        e = "dark-layout" !== a ? "dark-layout" : a === n ? "light-layout" : n || "light-layout", localStorage.setItem(T + "-prev-skin", a), localStorage.setItem(T + "-current-skin", e), function (a) {
            var e = t(".nav-link-style"), n = z(), o = t(".main-menu"), l = t(".header-navbar"), r = a || n;
            s.removeClass("semi-dark-layout dark-layout bordered-layout"), "dark-layout" === r ? (s.addClass("dark-layout"), o.removeClass("menu-light").addClass("menu-dark"), l.removeClass("navbar-light").addClass("navbar-dark"), e.find(".ficon").replaceWith(feather.icons.sun.toSvg({class: "ficon"}))) : "bordered-layout" === r ? (s.addClass("bordered-layout"), o.removeClass("menu-dark").addClass("menu-light"), l.removeClass("navbar-dark").addClass("navbar-light"), e.find(".ficon").replaceWith(feather.icons.moon.toSvg({class: "ficon"}))) : "semi-dark-layout" === r ? (s.addClass("semi-dark-layout"), o.removeClass("menu-dark").addClass("menu-light"), l.removeClass("navbar-dark").addClass("navbar-light"), e.find(".ficon").replaceWith(feather.icons.moon.toSvg({class: "ficon"}))) : (s.addClass("light-layout"), o.removeClass("menu-dark").addClass("menu-light"), l.removeClass("navbar-dark").addClass("navbar-light"), e.find(".ficon").replaceWith(feather.icons.moon.toSvg({class: "ficon"}))), t("input:radio[data-layout=" + r + "]").length > 0 && setTimeout((function () {
                t("input:radio[data-layout=" + r + "]").prop("checked", !0)
            }))
        }(e), t(".horizontal-menu .header-navbar.navbar-fixed").css({
            background: "inherit",
            "box-shadow": "inherit"
        }), t(".horizontal-menu .horizontal-menu-wrapper.header-navbar").css("background", "inherit")
    })), localStorage.getItem(T + "-current-skin")
}(window, document, jQuery), "function" == typeof jQuery.validator && jQuery.validator.setDefaults({
    errorElement: "span",
    errorPlacement: function (a, e) {
        e.parent().hasClass("input-group") || e.hasClass("select2") || "checkbox" === e.attr("type") ? a.insertAfter(e.parent()) : e.hasClass("form-check-input") ? a.insertAfter(e.parent().siblings(":last")) : a.insertAfter(e), e.parent().hasClass("input-group") && e.parent().addClass("is-invalid")
    },
    highlight: function (a, e, t) {
        $(a).addClass("error"), $(a).parent().hasClass("input-group") && $(a).parent().addClass("is-invalid")
    },
    unhighlight: function (a, e, t) {
        $(a).removeClass("error"), $(a).parent().hasClass("input-group") && $(a).parent().removeClass("is-invalid")
    }
});