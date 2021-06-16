var mod = new Vue({
  el: "#mod",
  data: () => ({
    id: '',
    nombre: '',
    apellido: '',
    direccion: '',
    fecha: '',
    sexo: '',
    email: '',
    departamento: '',
    barrio: '',
    iniDireccion: '',
    iniBarrio: '',
    iniEmail: '',
    iniDepartamento: '',
  }),
  mounted() {
    this.cargarDatosIniciales();
    iniDireccion = this.iniDireccion;
    iniBarrio = this.iniBarrio;
    iniEmail = this.iniEmail;
    iniDepartamento = this.iniDepartamento;
  },

  methods:
  {

    SetDatos() {


      email = this.email;
      console.log("Email:" + this.email);
      direccion = this.direccion;
      console.log("Direccion: " + this.direccion);
      departamento = this.departamento;
      console.log("Departamento: " + this.departamento);
      barrio = this.barrio;
      console.log("Barrio: " + this.barrio);



      axios.post("http://localhost:8080/grupo15-services/rest/usuario/ciudadano/modificar", {

        email: this.email.toString(),
        direccion: this.direccion.toString(),
        departamento: this.departamento.toString(),
        barrio: this.barrio.toString()
      });
    },
    cargarDatosIniciales() {

      axios.get("http://localhost:8080/grupo15-services/rest/usuario/ciudadano/datosModificar")
        .then((response => {
          console.log("GET rest: ", response.data.barrio)
          this.iniBarrio= response.data.barrio;
          this.iniDepartamento= response.data.departamento;
          this.iniDireccion= response.data.direccion;
          this.iniEmail= response.data.mail;

        }))

    }
  }
})



