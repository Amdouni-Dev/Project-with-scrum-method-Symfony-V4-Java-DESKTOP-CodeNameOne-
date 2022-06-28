window.onload=()=>{
    const FiltersForm= document.querySelector("#filters")



    document.querySelectorAll("#filters input").forEach(input => {
        input.addEventListener("change",()=>{
           // console.log("click")
            const Form= new FormData(FiltersForm);

            // n7adher l'url
            const Params= new URLSearchParams();

            //console.log(Form.entries())
            Form.forEach((value,key)=>{
            //        console.log(key,value);
            Params.append(key,value);
          //  console.log(Params.toString());

            });

            const Url= new URL (window.location.href);
           // console.log(url);
            // nabda fel ajax
            fetch(Url.pathname + "? "+Params.toString() + "&ajax=1", {
                headers: {
                    "X-Requested-With": "XMLHttpRequest"
                }
            }).then(response =>
                response.json()
             ).then( data => {
            //    console.log(data);
              // nlawej aal zone mte3 l contenu
              const content= document.querySelector("#content");
              // nbadel l contenu
              content.innerHTML =data.content;
              history.pushState({},null,Url.pathname + "?" +Params.toString());
                }

            )
                .catch(e=>alert(e));

        });
    });

}