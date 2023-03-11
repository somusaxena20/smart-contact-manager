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
