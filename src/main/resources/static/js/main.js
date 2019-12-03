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