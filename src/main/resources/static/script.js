$(document).ready(function () {

    // JavaScript for an owner adding a book to the store
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
                    url: '/getBooks',
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

    // JavaScript for an owner removing a book from the store
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
                    url: '/getBooks',
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

    // JavaScript for an owner to update a book in the bookstore
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
                    url: '/getBooks',
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

    // JavaScript for a customer to add a book to their cart
    $(document).on("submit", "#postCartForm", function (event) {

        const data = {
            isbn: $('#buyingISBN').val(),
            quantity: $('#buyingQuantity').val(),
            ID: $('#userID').val()
        };

        $.ajax({
            type: 'POST',
            url: '/customer/addBookToCart?id=' + data.ID + '&quantity=' + data.quantity + '&isbn=' + data.isbn,
            data: JSON.stringify(data),
            contentType: 'application/json',
        })
            .done(() => {
                // After a successful POST request, make a separate AJAX call to get updated book data
                $.ajax({
                    type: 'GET',
                    url: '/getBooks',
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

                $.ajax({
                    type: 'GET',
                    url: 'http://localhost:8080/getCart',
                    dataType: 'text',
                })
                    .done((updatedBooks) => {
                        // Log the updated book data to the console
                        console.log({ updatedBooks });

                        // Manipulate the DOM to update the book information
                        const booksContainer = $('#cart');
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

    // JavaScript for a customer to remove a book to their cart
    $(document).on("submit", "#deleteCartForm", function (event) {

        const data = {
            isbn: $('#removeISBN').val(),
            quantity: $('#removeQuantity').val(),
            ID: $('#currentUser').val()
        };

        $.ajax({
            type: 'DELETE',
            url: '/customer/removeItemFromCart?id=' + data.ID + '&quantity=' + data.quantity + '&isbn=' + data.isbn,
            data: JSON.stringify(data),
            contentType: 'application/json',
        })
            .done(() => {
                // After a successful POST request, make a separate AJAX call to get updated book data
                $.ajax({
                    type: 'GET',
                    url: '/getBooks',
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

                $.ajax({
                    type: 'GET',
                    url: 'http://localhost:8080/getCart',
                    dataType: 'text',
                })
                    .done((updatedBooks) => {
                        // Log the updated book data to the console
                        console.log({ updatedBooks });

                        // Manipulate the DOM to update the book information
                        const booksContainer = $('#cart');
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

    // JavaScript for a customer to checkout
    $(document).on("submit", "#checkoutCartForm", function (event) {

        const data = {
            ID: $('#checkoutUser').val()
        };

        $.ajax({
            type: 'POST',
            url: '/customer/checkoutUser?id=' + data.ID,
            data: JSON.stringify(data),
            contentType: 'application/json',
        })
            .done(() => {
                // After a successful POST request, make a separate AJAX call to get updated book data
                $.ajax({
                    type: 'GET',
                    url: '/getBooks',
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

                $.ajax({
                    type: 'GET',
                    url: 'http://localhost:8080/getCart',
                    dataType: 'text',
                })
                    .done((updatedBooks) => {
                        // Log the updated book data to the console
                        console.log({ updatedBooks });

                        // Manipulate the DOM to update the book information
                        const booksContainer = $('#cart');
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
