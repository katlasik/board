function registerCheck(fieldSelector) {
    const $field = $(fieldSelector)
    const $csrf = $("#csrf-token")

    $field.on("keydown", debounce(() => {

        const value = $field.val()

        $.get({
            url: "/registration/check",
            data: {
                field: $field.attr("name"),
                value: value
            },
            headers: {
                "X-CSRF-TOKEN": $csrf.val()
            },
            success: ({taken}) => {
                if (taken) {
                    $($field).prev("span").removeClass("hidden");
                } else {
                    $($field).prev("span").addClass("hidden");
                }
            }
        })

    }))
}

$(document).ready(() => {

    registerCheck("#name")
    registerCheck("#email")

})
