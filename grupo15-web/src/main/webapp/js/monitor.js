var res = new Vue({
    el: "#res",
    data: () => ({
        IdEnf: '',
        IdVac: '',
        IdPlan: '',
        listaEnfermedades: [],
        listaPlanes: [],
        listaVacunas: [],
        listEnfHardC: [
            {
            idEnfermedad: "covid",
            vacunados:10  
          },
          {
            idEnfermedad: "fiebre amarilla",
            vacunados:30
          },
          {
            idEnfermedad: "meningitis",
            vacunados:20  
          },
          {
            idEnfermedad: "hepatitis",
            vacunados:150  
          },
          {
            idEnfermedad: "tuberculosis",
            vacunados:80  
          },
          {
            idEnfermedad: "gripe",
            vacunados: 5  
          }
        ],

        vacunaVac: [
            {
            idVacuna: "Pfizer",
            vacunados:10  
          },
          {
            idVacuna: "Astrazeneca",
            vacunados:30
          },
          {
            idVacuna: "Sinovac",
            vacunados:20  
          },
          {
            idVacuna: "Sinopharm",
            vacunados:150  
          },
          {
            idVacuna: "Moderna",
            vacunados:80  
          },
          
        ],
        etiquetas:[], 
        valores: [], 
        etiquetas2:[], 
        valores2: [], 
        nroDia: '400',
        nroMes: '200',
        nroAnio: '1000',
        variablePrueba: '50%',
    }),




    mounted() {


        this.CargarEnfermedades();
       this.setGrafica1();
       this.Grafica2();
    },

    methods:
    {

        setGrafica1() {

            // Obtener una referencia al elemento canvas del DOM
            const $grafica = document.querySelector("#grafica");
            // Las etiquetas son las que van en el eje X. 
        
            for (i in this.listEnfHardC) {

              this.etiquetas.push(this.listEnfHardC[i].idEnfermedad)
                };

             for (i in this.listEnfHardC) {

                    this.valores.push(this.listEnfHardC[i].vacunados)
                      } ;     

            console.log("Lista Enfermedades", this.etiquetas);
            // Podemos tener varios conjuntos de datos. Comencemos con uno
            const datosVentas2020 = {
                label: "Total de vacunados por enfermedad",
                data: this.valores, // La data es un arreglo que debe tener la misma cantidad de valores que la cantidad de etiquetas
                backgroundColor: 'rgba(54, 162, 235, 0.2)', // Color de fondo
                borderColor: 'rgba(54, 162, 235, 1)', // Color del borde
                borderWidth: 1,// Ancho del borde
            };
            new Chart($grafica, {
                type: 'bar',// Tipo de gráfica
                data: {
                    labels: this.etiquetas,
                    datasets: [
                        datosVentas2020,
                        // Aquí más datos...
                    ]
                },
                options: {
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero: true
                            }
                        }],
                    },
                }
            });
        },

        setDatos() {

            IdEnf = this.IdEnf;
            console.log("IdEnf:" + this.IdEnf);
            IdPlan = this.IdPlan;
            console.log("IdPlan:" + this.IdPlan);
            IdVac = this.IdVac;
            console.log("IdVac:" + this.IdVac);
            
        },

        CargarEnfermedades() {

            axios.get("http://localhost:8080/grupo15-services/rest/reservas/enf/")
                .then((response => {
                    console.log("GET enfermedades: ", response.data)
                    this.listaEnfermedades = response.data;
                }))

        },

        SeleccionarEnfermedades() {
            IdEnf = this.IdEnf;
            axios.get("http://localhost:8080/grupo15-services/rest/reservas/enf/" + this.IdEnf.toString())
                .then((response => {
                    console.log("GET planes: ", response.data)
                    this.listaPlanes = response.data
                    console.log("planes: " + this.listaPlanes.nombre);
                }))

        },


        SeleccionarPlanes() {

            IdPlan = this.IdPlan;
            axios.get("http://localhost:8080/grupo15-services/rest/reservas/pv" + "?p=" + this.IdPlan.toString())
            this.CargarVacunas();

        },

        CargarVacunas() {

            axios.get("http://localhost:8080/grupo15-services/rest/reservas/vac/")
                .then((response => {
                    console.log("GET vacunas: ", response.data)
                    this.listaVacunas = response.data
                }))

        },

        SeleccionarVacunas() {
            IdVac = this.IdVac;
        },

        Grafica2() {
            // Obtener una referencia al elemento canvas del DOM
const $grafica2 = document.querySelector("#grafica2");
// Las etiquetas son las porciones de la gráfica


for (i in this.vacunaVac) {

    this.etiquetas2.push(this.vacunaVac[i].idVacuna)
      };

   for (i in this.vacunaVac) {

          this.valores2.push(this.vacunaVac[i].vacunados)
            } ;     

// Podemos tener varios conjuntos de datos. Comencemos con uno
const datosIngresos = {
    data: this.valores2, // La data es un arreglo que debe tener la misma cantidad de valores que la cantidad de etiquetas
    // Ahora debería haber tantos background colors como datos, es decir, para este ejemplo, 4
    backgroundColor: [
        'rgba(163,221,203,0.2)',
        'rgba(232,233,161,0.2)',
        'rgba(230,181,102,0.2)',
        'rgba(229,112,126,0.2)',
    ],// Color de fondo
    borderColor: [
        'rgba(163,221,203,1)',
        'rgba(232,233,161,1)',
        'rgba(230,181,102,1)',
        'rgba(229,112,126,1)',
    ],// Color del borde
    borderWidth: 1,// Ancho del borde
};
new Chart($grafica2, {
    type: 'pie',// Tipo de gráfica. Puede ser dougnhut o pie
    data: {
        labels: this.etiquetas2,
        datasets: [
            datosIngresos,
            // Aquí más datos...
        ]
    },
});
        }//cierra funcion

    }


})


