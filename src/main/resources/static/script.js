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
            url: '/owner',
            data: JSON.stringify(data),
            contentType: 'application/json',
        })
            .done(() => {
                // After a successful POST request, make a separate AJAX call to get updated book data
                $.ajax({
                    type: 'GET',
                    url: 'http://localhost:8080/getBooks',
                    dataType: 'text',
                })
                    .done((updatedBooks) => {
                        // Log the updated book data to the console
                        console.log({ updatedBooks });

                        // Manipulate the DOM to update the book information
                        const booksContainer = $('#inventory');
                        booksContainer.text(updatedBooks);
                    })
                    .fail((err) => {
                        console.error(err);
                    });
            })
            .fail((err) => {
                console.error(err);
            })
            .always(() => {
                console.log('always called');
            });
        event.preventDefault();
    });

    $(document).on("submit", "#deleteForm", function (event) {

        const data = {
            isbn: $('#deletedISBN').val(),
        };

        $.ajax({
            type: 'DELETE',
            url: '/owner?isbn=' + data.isbn,
            data: JSON.stringify(data),
            contentType: 'application/json',
        })
            .done(() => {

                console.log(data)
                // After a successful DELETE request, make a separate AJAX call to get updated book data
                $.ajax({
                    type: 'GET',
                    url: 'http://localhost:8080/getBooks',
                    dataType: 'text',
                })
                    .done((updatedBooks) => {
                        // Log the updated book data to the console
                        console.log({ updatedBooks });

                        // Manipulate the DOM to update the book information
                        const booksContainer = $('#inventory');
                        booksContainer.text(updatedBooks);

                    })
                    .fail((err) => {
                        console.error(err);
                    });
            })
            .fail((err) => {
                console.error(err);
            })
            .always(() => {
                console.log('always called');
            });
        event.preventDefault();
    });

    $(document).on("submit", "#patchForm", function (event) {

        const data = {
            title: $('#newTitle').val(),
            author: $('#newAuthor').val(),
            isbn: $('#updateIsbn').val(),
            price: $('#newPrice').val(),
            quantity: $('#newQuantity').val()
        };

        $.ajax({
            type: 'PATCH',
            url: '/owner?isbn=' + data.isbn + "&quantity=" + data.quantity + "&author=" + data.author + "&title=" + data.title + "&price=" + data.price,
            data: JSON.stringify(data),
            contentType: 'application/json',
        })
            .done(() => {

                console.log(data)

                // After a successful PATCH request, make a separate AJAX call to get updated book data
                $.ajax({
                    type: 'GET',
                    url: 'http://localhost:8080/getBooks',
                    dataType: 'text',
                })
                    .done((updatedBooks) => {
                        // Log the updated book data to the console
                        console.log({ updatedBooks });

                        // Manipulate the DOM to update the book information
                        const booksContainer = $('#inventory');
                        booksContainer.text(updatedBooks);

                    })
                    .fail((err) => {
                        console.error(err);
                    });
            })
            .fail((err) => {
                console.error(err);
            })
            .always(() => {
                console.log('always called');
            });
        event.preventDefault();
    });
});
