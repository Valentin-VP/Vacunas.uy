
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

          console.log(`lista constancias: `, response.data)
          this.listConstancias = response.data

          console.log(`lista constancias: `, this.listConstancias)
        }))
    },
    


    cargarDatos() {


      for (i in this.listConstancias) {

        this.etiquetas.push(this.listConstancias[i].idEnfermedad)
      };

    

    }

  }



})
