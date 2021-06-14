var mod = new Vue({
  el: "#mod",
  data: () => ({
    id:'',
    nombre:'',
    apellido:'',
    direccion:'',
    fecha:'',
    sexo:'',
    email:'',
    departamento: '',
    barrio:''

  }),

  methods: 
  {
    
    SetDatos () {
    

    email=this.email;
    console.log("Email:" + this.email);
    direccion = this.direccion;
    console.log("Direccion: " + this.direccion);
    departamento = this.departamento;
    console.log("Departamento: " + this.departamento);
    barrio = this.barrio;
    console.log("Barrio: " + this.barrio);
    
    axios.post("http://localhost:8080/grupo15-services/rest/registro/ciudadano", {
      
       email : this.email.toString(),
      direccion: this.direccion.toString(),
      departamento: this.departamento.toString(),
      barrio: this.barrio.toString()
    });
 

      }
  }
   

})
