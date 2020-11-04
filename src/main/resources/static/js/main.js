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

    const socket = new SockJS('/board-ws');
    const stompClient = Stomp.over(socket);
    const msg = $("#new-question-msg").html();
    const loggedInUser = $("#user-name").html()

    stompClient.connect({}, () => {

        stompClient.subscribe("/updates", data => {

           const body = JSON.parse(data.body)

           if(body.originalPosterName === loggedInUser && body.anwserPosterName !== loggedInUser) {
               new PNotify({
                   styling: "bootstrap3",
                   text: `<a href='/question/${body.questionId}'>${msg}</a>`
               });
           }
        });

    })


    $('.file input').change(function(){
        const files = $(this)[0].files;
        const text = $(this).prev().html().replace("{0}", files.length)
        $('.file label').html(text)
    });

    stompClient.subscribe(

    )

})

