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
  }),
  mounted() {
    this.cargarDatosIniciales();
  },

  methods:
  {

    SetDatos() {
      axios.post("/grupo15-services/rest/usuario/ciudadano/modificar", {

        email: this.email.toString(),
        direccion: this.direccion.toString(),
        departamento: this.departamento.toString(),
        barrio: this.barrio.toString()
      });
    },

    cargarDatosIniciales() {
      let _this = this
      axios.get("/grupo15-services/rest/usuario/ciudadano/datosModificar")
        .then((response => {
          console.log("GET rest: ", response.data)
          _this.barrio= response.data.barrio;
          _this.departamento= response.data.departamento;
          _this.direccion= response.data.direccion;
          _this.email= response.data.mail;

        }))
    }
  }
})



