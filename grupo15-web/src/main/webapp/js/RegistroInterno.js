var res = new Vue({
  el: "#res",
  data: () => ({
    id: '',
    nombre: '',
    apellido: '',
    direccion: '',
    fecha: '',
    sexo: '',
    email: '',
    password: '',
    tipoUser: '',
    barrio: '',
    departamento: '',
    ok200:''

  }),

  methods:
  {

    SetDatos() {
      id = this.id;
      console.log("id:" + this.id);
      nombre = this.nombre;
      console.log("nombre:" + this.nombre);
      apellido = this.apellido;
      console.log("apellido:" + this.apellido);
      fecha = this.fecha;
      console.log("Fecha:" + this.fecha);
      sexo = this.sexo;
      console.log("Sexo:" + this.sexo);
      email = this.email;
      console.log("Email:" + this.email);
      password = this.password;
      console.log("Password:" + this.password);
      direccion = this.direccion;
      console.log("Direccion:" + this.direccion);
      tipoUser = this.tipoUser.charAt(0).toUpperCase() + this.tipoUser.slice(1);
      console.log("TipoUser:" + this.tipoUser);
      barrio = this.barrio;
      console.log("barrio:" + this.barrio);
      departamento = this.departamento;
      console.log("Departamento:" + this.departamento);

      this.enviar();

    },

    enviar() {
      tok = this.id + ':' + this.password;
      hash = Base64.encode(tok);
      console.log("Tok:" + this.tok);
      console.log("Hash:" + this.hash);
      Basic = 'Basic ' + hash;
      console.log("Basic:" + this.Basic);
      axios.defaults.headers.common["Authorization"] = Basic;
      let config = { "Authorization": this.Basic };
      axios.post('/grupo15-services/rest/registro/interno/', 
        {
          tipoUser: this.tipoUser.toString(),
          nombre: this.nombre.toString(),
          apellido: this.apellido.toString(),
          fecha: this.fecha.toString(),
          sexo: this.sexo.toString(),
          direccion: this.direccion.toString(),
          email: this.email.toString(),
          barrio: this.barrio.toString(),
          departamento: this.departamento.toString()
        }, {
          headers: { config }
      })
      .then(response => {
        if (response.status === 201) {
          console.log("Respuesta: " + response.status)
          this.ok200="Se agregó al usuario correctamente"
          console.log("VariableRespuesta: " + this.ok200)
        } })
      .catch(error => {
          if (error.response.status === 400) {
  
           this.ok200="El usuario ya existe";
       }
         
  
      });
  

    }

    // axios.post("http://localhost:8080/grupo15-services/rest/reservas/confirmar", {
    // id : this.id.toString(),
    // nombre : this.nombre.toString(),
    // apellido : this.apellido.toString(),
    // fecha : this.fecha.toString(),
    // sexo : this.sexo.toString(),
    //  email : this.email.toString(),
    // });


  }

     
  
})
