function registerCheck(fieldSelector) {
    const $field = $(fieldSelector)
    const $csrf = $("#csrf-token")

    $field.on("keydown", () => {
        $($field).prev("span").addClass("hidden")
        $($field).removeClass("error")
    })


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
                    $($field).addClass("error");
                    $($field).prev("span").removeClass("hidden");
                }
            }
        })

    }))
}

$(document).ready(() => {

    registerCheck("#name")
    registerCheck("#email")

})
