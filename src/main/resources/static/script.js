$(document).ready(function () {

    // Function to update books in the inventory
    function updateInventory() {
        $.ajax({
            type: 'GET',
            url: '/getBooks',
            dataType: 'json',  // Update to JSON
        })
            .done((updatedBooks) => {
                console.log({ updatedBooks });

                // Manipulate the DOM to update the book information
                const booksContainer = $('#inventory');
                booksContainer.empty(); // Clear previous content

                // Create an unordered list for the books
                const booksList = $('<ul>').addClass('book-list');

                // Iterate over the array of books and create list items for each
                updatedBooks.forEach((book) => {
                    const title = book.title;
                    const author = book.author;
                    const isbn = book.isbn;
                    const price = book.price;
                    const quantityAvailable = book.quantity;

                    const bookItem = $('<li>')
                        .addClass('book-item')
                        .text(`Title: ${title} | Author: ${author} | ISBN: ${isbn} | Price: ${price} | Quantity available: ${quantityAvailable}`)
                        .data('isbn', isbn);

                    // Add a click event to toggle a class for highlighting
                    bookItem.click(function () {
                        $(this).toggleClass('highlighted');
                    });

                    booksList.append(bookItem);
                });

                // Append the list to the inventory container
                booksContainer.append(booksList);
            })
            .fail((err) => {
                console.error(err);
            });
    }

    function updateCart() {
        $.ajax({
            type: 'GET',
            url: '/getCart',
            dataType: 'json',
        })
            .done((updatedCart) => {
                console.log({ updatedCart });

                // Manipulate the DOM to update the cart information
                const cartContainer = $('#cart');
                cartContainer.empty(); // Clear previous content

                // Create an unordered list for the cart items
                const cartList = $('<ul>').addClass('cart-list');

                // Iterate over the array of cart items and create list items for each
                updatedCart.forEach((cartItem) => {
                    const title = cartItem.Title;
                    const author = cartItem.Author;
                    const isbn = cartItem.ISBN;
                    const price = cartItem.Price;
                    const available = cartItem.Available;
                    const quantity = cartItem.Quantity;

                    // Create list item with data attributes for ISBN and quantity
                    const cartItemElement = $('<li>')
                        .addClass('cart-item')
                        .text(`Title: ${title} | Author: ${author} | ISBN: ${isbn} | Price: ${price} | Quantity available: ${available} | Amount in cart: ${quantity}`)
                        .data({
                            'isbn': isbn,
                            'quantity': quantity
                        });

                    // Add a click event to toggle a class for highlighting
                    cartItemElement.click(function () {
                        $(this).toggleClass('highlighted');
                    });

                    cartList.append(cartItemElement);
                });

                // Append the list to the cart container
                cartContainer.append(cartList);
            })
            .fail((err) => {
                console.error(err);
            });
    }

    // update upon loading page
    updateInventory();
    updateCart();


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
                // After a successful POST request, update inventory and cart
                updateInventory();
                updateCart();
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
                // After a successful POST request, update inventory and cart
                updateInventory();
                updateCart();
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

                // After a successful POST request, update inventory and cart
                updateInventory();
                updateCart();
            })
            .fail((err) => {
                console.error(err);
            })
            .always(() => {
                console.log('always called');
            });
        event.preventDefault();
    });

    $(document).on("click", "#btnPurchaseBooks", function (event) {
        $.ajax({
            type: 'GET',
            url: '/getCart',
        })
            .done((displayCart) => {
                console.log({ displayCart });

                // Manipulate the DOM to update the cart information
                const cartContainer = $('#checkoutCartForm');
                cartContainer.empty(); // Clear previous content

                // Create an unordered list for the cart items
                const cartList = $('<ul>').addClass('cart-list');

                // Iterate over the array of cart items and create list items for each
                displayCart.forEach((cartItem) => {
                    const title = cartItem.Title;
                    const author = cartItem.Author;
                    const isbn = cartItem.ISBN;
                    const price = cartItem.Price;
                    const available = cartItem.Available;
                    const quantity = cartItem.Quantity;

                    // Create list item with data attributes for ISBN and quantity
                    const cartItemElement = $('<li>')
                        .addClass('cart-item')
                        .text(`Title: ${title} | Author: ${author} | ISBN: ${isbn} | Price: ${price} | Quantity available: ${available} | Amount in cart: ${quantity}`)
                        .data({
                            'isbn': isbn,
                            'quantity': quantity
                        });

                    cartList.append(cartItemElement);
                });

                // Append the list to the cart container
                cartContainer.append(cartList);
            })
            .fail((err) => {
                console.error(err);
            })
            .always(() => {
                console.log('always called');
            });
    });


    // JavaScript for a customer to add a book to their cart
    $(document).on("submit", "#postCartForm", function (event) {

        const data = {
            isbn: $('#buyingISBN').val(),
            quantity: $('#buyingQuantity').val(),
        };

        const userID = document.getElementById("userID").innerText

        $.ajax({
            type: 'POST',
            url: '/customer/addBookToCart?id=' + userID + '&quantity=' + data.quantity + '&isbn=' + data.isbn,
            data: JSON.stringify(data),
            contentType: 'application/json',
        })
            .done(() => {
                // After a successful POST request, update inventory and cart
                updateInventory();
                updateCart();
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

        const userID = document.getElementById("userID").innerText

        $.ajax({
            type: 'DELETE',
            url: '/customer/removeItemFromCart?id=' + userID + '&quantity=' + data.quantity + '&isbn=' + data.isbn,
            data: JSON.stringify(data),
            contentType: 'application/json',
        })
            .done(() => {
                // After a successful POST request, update inventory and cart
                updateInventory();
                updateCart();
            })
            .fail((err) => {
                console.error(err);
            })
            .always(() => {
                console.log('always called');
            });
        event.preventDefault();
    });

    // JavaScript for a customer to check out
    $(document).on("submit", "#checkoutCartForm", function (event) {

        const data = {
            ID: $('#checkoutUser').val()
        };

        const userID = document.getElementById("userID").innerText

        $.ajax({
            type: 'POST',
            url: '/customer/checkoutUser?id=' + userID,
            data: JSON.stringify(data),
            contentType: 'application/json',
        })
            .done(() => {
                // After a successful POST request, update inventory and cart
                updateInventory();
                updateCart();
            })
            .fail((err) => {
                console.error(err);
            })
            .always(() => {
                console.log('always called');
            });
        event.preventDefault();
    });

    // JavaScript to get recommended books
    $(document).on("click", "#btnGetRecommendations", function () {
        const userID = document.getElementById("userID").innerText;

        $.ajax({
            type: 'GET',
            url: '/recommendations?userId=' + userID,
            dataType: 'text',
        })
            .done((recommendations) => {
                console.log({ recommendations });

                // Manipulate the DOM to display the recommended books in the modal
                const recommendationsContainer = $('#recommendationsContainer');
                recommendationsContainer.empty(); // Clear previous content

                // Split the string into an array of lines
                const lines = recommendations.split('\n');

                // Iterate over lines and create separate HTML elements for each
                lines.forEach((line) => {
                    const bookElement = $('<p>').text(line);
                    recommendationsContainer.append(bookElement);
                });

                // Show the modal
                $('#myModal').modal('show');
            })
            .fail((err) => {
                console.error(err);
            });
    });

    // Add event listener for the button to add 1 of each selected book to the cart
    $(document).on("click", "#btnAddSelectedToCart", function () {
        // Get all highlighted items
        const highlightedItems = $('.highlighted');

        // Iterate over highlighted items and add each to the cart
        highlightedItems.each(function () {
            const isbn = $(this).data('isbn'); // Assuming you have a data attribute for ISBN
            const quantity = 1; // You can customize the quantity as needed

            const data = {
                isbn: isbn,
                quantity: quantity
            };

            const userID = document.getElementById("userID").innerText;

            $.ajax({
                type: 'POST',
                url: '/customer/addBookToCart?id=' + userID + '&quantity=' + data.quantity + '&isbn=' + data.isbn,
                data: JSON.stringify(data),
                contentType: 'application/json',
            })
                .done(() => {
                    // After a successful POST request, update inventory and cart
                    updateInventory();
                    updateCart();
                })
                .fail((err) => {
                    console.error(err);
                })
                .always(() => {
                    console.log('always called');
                });
        });
    });

    $(document).on("click", "#btnRemoveSelectedFromCart", function () {
        // Get all highlighted cart items
        const highlightedItems = $('.cart-item.highlighted');

        // Iterate over highlighted items and remove each one
        highlightedItems.each(function () {
            const isbn = $(this).data('isbn');
            const quantity = $(this).data('quantity');

            // Call the server-side endpoint to remove the item from the cart
            const userID = document.getElementById("userID").innerText;
            $.ajax({
                type: 'DELETE',
                url: '/customer/removeItemFromCart?id=' + userID + '&quantity=' + quantity + '&isbn=' + isbn,
                contentType: 'application/json',
            })
                .done(() => {
                    // After a successful DELETE request, update inventory and cart
                    updateInventory();
                    updateCart();
                })
                .fail((err) => {
                    console.error(err);
                });
        });
    });
});
