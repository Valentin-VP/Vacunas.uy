var res = new Vue({
    el: "#res",
    data: () => ({
      id:'',
      nombre:'',
      apellido:'',
      direccion:'',
      fecha:'',
      sexo:'',
      email:'',

    }),

    methods: 
    {
      
      SetDatos () {
      id=this.id;
      console.log("IdEnf:" + this.id);
      nombre=this.nombre;
      console.log("IdPlan:" + this.nombre);
      apellido= this.apellido;
      console.log("IdVac:" + this.apellido);
      fecha=this.fecha;
      console.log("Fecha:" + this.fecha);
      sexo=this.sexo;
      console.log("Sexo:" + this.sexo);
      email=this.email;
      console.log("Email:" + this.email);
      direccion = this.direccion;
      console.log("Direccion: " + this.direccion);
      axios.post("http://localhost:8080/grupo15-services/rest/registro/ciudadano", {
        id : this.id.toString(),
        nombre : this.nombre.toString(),
        apellido : this.apellido.toString(),
        fechaNac : this.fecha.toString(),
        sexo : this.sexo.toString(),
         email : this.email.toString(),
        direccion: this.direccion.toString(),
      });
   
  
        }
    }
     
  
})
