$(document).ready(function () {
    $(document).on("submit", "#postForm", function (event) {

        const data = {
            title: $('#title').val(),
            author: $('#author').val(),
            isbn: $('#isbn').val(),
            price: $('#price').val(),
            quantity: $('#quantity').val()
        };

        $.ajax({
            type: 'POST',
            url: 'localhost:8080/owner',
            data: JSON.stringify(data),
            contentType: 'application/json',
          })
            .done((data) => {
              console.log({ data });
            })
            .fail((err) => {
              console.error(err);
            })
            .always(() => {
              console.log('always called');
            });
        alert("created!");
        event.preventDefault();
    });
});