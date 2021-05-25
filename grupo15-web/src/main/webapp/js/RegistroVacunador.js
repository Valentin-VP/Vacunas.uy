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
      departamento:'',
      barrio:''

    }),

    methods: 
    {
      
      SetDatos () {
      id=this.id; 
      console.log("id:" + this.id);
      nombre=this.nombre;
      console.log("nombre:" + this.nombre);
      apellido= this.apellido;
      console.log("apellido:" + this.apellido);
      fecha=this.fecha;
      console.log("Fecha:" + this.fecha);
      sexo=this.sexo;
      console.log("Sexo:" + this.sexo);
      email=this.email;
      console.log("Email:" + this.email);
      barrio=this.barrio;
      console.log("Barrio:" + this.barrio);
      departamento=this.departamento;
      console.log("Departamento:" + this.departamento);
     axios.post("http://localhost:8080/grupo15-services/rest/registro/vacunador", {
      id : this.id.toString(),
      nombre : this.nombre.toString(),
      apellido : this.apellido.toString(),
      fechaNac : this.fecha.toString(),
      sexo : this.sexo.toString(),
       email : this.email.toString(),
      direccion: this.direccion.toString(),
      departamento: this.departamento.toString(),
      barrio: this.barrio.toString()
  
        })
    }
     
  }
})
