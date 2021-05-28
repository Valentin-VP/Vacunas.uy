var int = new Vue({
    el: "#int",
    data: {
      user: '',
      pass: '',
      error401:''
    
    },
   
  
  
    methods: {
      setDatos() {
  
        user = this.user;
        console.log("user:" + this.user);
        pass = this.pass;
        console.log("pass:" + this.pass);
        this.login();
  
      },
     
      login() {
        tok = this.user+':'+this.pass;
        hash = Base64.encode(tok);
        console.log("Tok:" + this.tok);
        console.log("Hash:" + this.hash);
        Basic = 'Basic ' + hash;
        console.log("Basic:" + this.Basic);
        axios.defaults.headers.common["Authorization"] = Basic;
        let config = {"Authorization": this.Basic};
       axios.post('http://localhost:8080/grupo15-services/rest/internalauth/login', {headers : {config}})
        .then(response =>{
          if (response.data.url){
            console.log("Respuesta: " + response.data.url)
            window.location.href = response.data.url
          }
          else{
            console.log("No se obtuvo el parametro url en el body. Respuesta fue:  " + response.data)
          }
        }
        )
        .catch(error => {
          if (error.response.status === 401) {
   		    console.log("Acceso no permitido. Verifique credenciales");
           this.error401="Acceso no permitido. Verifique credenciales";
 		  }
          else{
          	console.log(error)
          }
        })
   }
  
    }
  
  })
  