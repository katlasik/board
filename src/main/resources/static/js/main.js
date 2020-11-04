function debounce(callback) {
    let timeout = null

    return function() {
        const callNow = !timeout
        const next = () => callback.apply(this, arguments)

        clearTimeout(timeout)
        timeout = setTimeout(next, 1000)

        if (callNow) {
            next()
        }
    }
}

$(document).ready(() => {



    $('.file input').change(function(){
        const files = $(this)[0].files;
        const text = $(this).prev().html().replace("{0}", files.length)
        $('.file label').html(text)
    });

})

