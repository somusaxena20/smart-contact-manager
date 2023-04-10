console.log("calling js file....");

const search = () => {
  console.log("searching...");

  let query = $("#search-input").val();

  if (query == "") {
    $(".search-result").hide();
  } else {
    let uri = `http://localhost:1000/search/${query}`;

    fetch(uri)
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        console.log(data);

        let text = `<div class = 'list-group'>`;

        data.forEach((contact) => {
          console.log(`this is contact data : `);
          console.log(contact);
          text += `<a href='/user/${contact.cid}/contact' class='list-group-item list-group-action'> ${contact.name} </a>`;
          console.log("inside data for each loop...");
        });

        text += `</div>`;

        $(".search-result").html(text);

        $(".search-result").show();
      });
  }
};

//FIRST REQUEST TO SERVER TO CREATE ORDER

const paymentStart = () => {
  console.log("payment started...");
  let amount = $("#payment_field").val();
  console.log(amount);

  if (amount == "" || amount == null) {
    alert("Amount cannot blank");
    return;
  }

  // USING AJAX TO SEND REQUEST TO SERVER TO CREATE ORDER

  $.ajax({
    url: "/user/create-order",
    data: JSON.stringify({ amount: amount, info: "order_request" }),
    contentType: "application/json",
    type: "POST",
    dataType: "json",
    success: function (response) {
      //invoked when success
      console.log(response);

      let options = {
        key: "rzp_test_PGCW1kWaawKmNi",
        amount: response.amount,
        currency: "INR",
        name: "Smart Contact Manager",
        description: "Donation",
        image:
          "https://yt3.ggpht.com/yti/AHXOFjXAXYjzcOPKEu7y8sPHVVQ4oDb_t4JsxwQYooU7=s88-c-k-c0x00ffffff-no-rj-mo",
        order_id: response.id,
        handler: function (response) {
          console.log(response.razorpay_payment_id);
          console.log(response.razorpay_order_id);
          console.log(response.razorpay_signature);
          console.log("Payment Successful !!");

          updatePaymentOnServer(response.razorpay_payment_id, response.razorpay_order_id, "paid");

          alert("Congrats ! Payment Successful !!");
        },
        prefill: {
          name: "", //your customer's name
          email: "",
          contact: "",
        },
        notes: {
          address: "SOMU SAXENA PAYMENT",
        },
        theme: {
          color: "#3399cc",
        },
      };

      let razorPay = new Razorpay(options);

      razorPay.on("payment.failed", function (response) {
        console.log(response.error.code);
        console.log(response.error.description);
        console.log(response.error.source);
        console.log(response.error.step);
        console.log(response.error.reason);
        console.log(response.error.metadata.order_id);
        console.log(response.error.metadata.payment_id);
      });

      razorPay.open();
    },
    error: function (error) {
      //invoke when error occur

      console.log(error);
      alert("Something went wrong !!");
    },
  });
};

function updatePaymentOnServer(payment_id, order_id, paymentStatus)
{
  $.ajax({
    url: "/user/update-order",
    data: JSON.stringify({ payment_id: payment_id, order_id: order_id, paymentStatus: paymentStatus }),
    contentType: "application/json",
    mode:"cors",
    type: "POST",
    dataType: "json",
    success:function(response) {
      console.log("Payment Successful...");
    },
    error:function(error){
      console.log("Payment Successful But Payment not comes to us...");
    }
  })
}
