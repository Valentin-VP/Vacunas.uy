var pass = new Vue({
    el: "#pass",
    data: {
      user: 'cookie',
      pass: '',
      codigo200:''
    },
   
  
  
    methods: {
      setDatos() {
  
        
        console.log("user:" + this.user);
        pass = this.pass;
        console.log("pass:" + this.pass);
        this.setPass();
  
      },
     
      setPass() {
        tok = this.pass;
        hash = Base64.encode(tok);
        console.log("Tok:" + this.tok);
        console.log("Hash:" + this.hash);
       
      
       axios.post('http://localhost:8080/grupo15-services/rest/usuario/cambiarpass', { 
        pass : this.hash.toString(),
       
      })
      
        .then(response =>{
          if (response.data.status=== 200){
            console.log("Respuesta: " + response.data)
            this.codigo200= response.data;
          }
          
        }
        )
      //  .catch(error => {
      //    if (error.response.status === 401) {
   		//    console.log("Acceso no permitido. Verifique credenciales");
 		 // }
       //   else{
         // 	console.log(error)
         // }
       // })
   }
  
    }
  
  })
  