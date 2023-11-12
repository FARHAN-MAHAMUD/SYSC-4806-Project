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
            type: $("#postForm").attr("method"),
            url: '/owner',
            data: JSON.stringify(data),
            contentType: 'application/json'
        }).done(function (response) {
            let body = response.substring(response.indexOf("<body>") + 6, response.indexOf("</body>"));
            $("body").html(body);
        });
        alert("created!");
        event.preventDefault();
    });
});