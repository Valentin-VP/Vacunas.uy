var int = new Vue({
    el: "#int",
    data: {
      user: '',
      pass: '',
    
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
       axios.post('http://localhost:8080/grupo15-services/rest/internalauth/login', {headers : { 'Auth' : Basic }})
       
   }
  
    }
  
  })
  