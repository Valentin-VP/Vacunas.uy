
var vac = new Vue({
  el: "#vac",
  data: {
    idEnf:'',
    enfermedad: '',
    id: '',
    inmunidad: '',
    totalDosis: '',
    ultimaDosis: '',
    vacuna: '',
    listConstancias: [],
    listEnf: [],
  },
  mounted() {
    this.traerDatos();
  },


  methods: {
    setDatos() {

      enfermedad = this.enfermedad;
      console.log("enfermedad seleccionada:" + this.enfermedad);
      this.obtenerCertificado();

    },
    traerDatos() {
      axios.get("http://localhost:8080/grupo15-services/rest/vacunaciones/certificadovue")
        .then((response => {

          console.log(`lista constancias: `, response.data)
          this.listConstancias = response.data

          console.log(`lista constancias: `, this.listConstancias)
          this.cargarEnfermedad();
        }))
    },
    


    cargarEnfermedad() {


      for (i in this.listConstancias) {

        this.listEnf.push(this.listConstancias[i].enfermedad)
      };
      console.log(`lista Enfermedades: `, this.listEnf)
    

    },

    obtenerCertificado() {


      for (i in this.listConstancias) {
        if (this.listConstancias[i].enfermedad = enfermedad){
          this.idEnf = this.enfermedad;
          this.id= this.listConstancias[i].ci;
          this.inmunidad= this.listConstancias[i].pInmunidad;
          this.totalDosis=this.listConstancias[i].totalDosis;
          this.ultimaDosis=this.listConstancias[i].ultimaDosis;
          this.vacuna=this.listConstancias[i].vacuna;
          console.log(`lista listEnf: `, this.idEnf);
          console.log(`lista id: `, this.id);
          console.log(`lista inmunidad: `, this.inmunidad);
          console.log(`lista totalDosis: `, this.totalDosis);
          console.log(`lista UltimaDosis: `, this.ultimaDosis);
          console.log(`lista Vacuna: `, this.vacuna);
        }
        
      };
  
    

    }

  }



})
