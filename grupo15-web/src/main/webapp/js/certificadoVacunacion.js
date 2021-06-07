
var vac = new Vue({
  el: "#vac",
  data: {
    enfermedad: '',
    id: '',
    inmunidad: '',
    totalDosis: '',
    ultimaDosis: '',
    vacuna: '',
    listConstancias: [],
  },
  mounted() {
    this.traerDatos();
  },


  methods: {
    setDatos() {

      enf = this.enfermedad;
      console.log("enfermedad seleccionada:" + this.enfermedad);


    },
    traerDatos() {
      axios.get("http://localhost:8080/grupo15-services/rest/vacunaciones/certificado")
        .then((response => {

          console.log(`lista Vacunatorios: `, response.data)
          this.listConstancias = response.data


        }))
    },



    cargarDatos() {


      for (i in this.listConstancias) {

        this.etiquetas.push(this.listConstancias[i].idEnfermedad)
      };

      for (i in this.listEnfHardC) {

        this.valores.push(this.listEnfHardC[i].vacunados)
      };

    }

  }



})
