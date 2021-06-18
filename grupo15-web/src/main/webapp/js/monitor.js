var res = new Vue({
    el: "#res",
    data: () => ({
        IdEnf: '',
        IdVac: '',
        IdPlan: '',
        listaEnfermedades: [],
        listaPlanes: [],
        listaVacunas: [],
        listVacPorVac: [],
        listVacPorEnf: [],


        etiquetas: [],
        valores: [],
        etiquetas2: [],
        valores2: [],
        nroDia: '',
        nroMes: '',
        nroAnio: '',

    }),




    mounted() {


        this.CargarEnfermedades();
        this.setGrafica1();
        this.CargarVacunadosPorVacuna();
        this.CargarVacunadosPorEnf();

    },

    methods:
    {

        setGrafica1() {

            // Obtener una referencia al elemento canvas del DOM
            const $grafica = document.querySelector("#grafica");
            // Las etiquetas son las que van en el eje X. 

            for (i in this.listVacPorEnf) {

                this.etiquetas.push(this.listVacPorEnf[i].idEnfermedad)
            };

            for (i in this.listVacPorEnf) {

                this.valores.push(this.listVacPorEnf[i].vacunados)
            };

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
            this.cargarDiaMesAnio();

        },




        CargarVacunadosPorVacuna() {

            axios.get("/grupo15-services/rest/monitor/vacunadosporvacs")
                .then((response => {
                    console.log("GET Vacunados Por Vac: ", response.data)
                    this.listVacPorVac = response.data;
                    console.log("List Vacunados Por Vacuna: ", this.listVacPorVac);
                    this.Grafica2();
                }))

        },

        CargarEnfermedades() {

            axios.get("/grupo15-services/rest/enfermedad/listar")
                .then((response => {
                    console.log("GET enfermedades: ", response.data)
                    this.listaEnfermedades = response.data;
                }))

        },

        SeleccionarEnfermedades() {
            IdEnf = this.IdEnf;
            if (IdEnf != '') {
                axios.get("/grupo15-services/rest/monitor/enf/" + this.IdEnf.toString())
                    .then((response => {
                        console.log("GET planes: ", response.data)
                        this.listaPlanes = response.data
                        console.log("planes: " + this.listaPlanes);
                        this.SeleccionarPlanes();
                    }))
            }
        },


        SeleccionarPlanes() {
            if(this.IdEnf != null && this.IdPlan != null){
                axios.get("/grupo15-services/rest/monitor/pv/" + this.IdEnf.toString() + "/" + this.IdPlan.toString())
                    .then((response => {
                        console.log("GET vacunas: ", response.data)
                        this.listaVacunas = response.data
                        console.log("List vacunas: ", this.listaVacunas)
                    }))
            }              
        },

        CargarVacunadosPorEnf() {

            axios.get("/grupo15-services/rest/monitor/vacunadosporenf")
                .then((response => {
                    console.log("GET Vacunados Por Enf: ", response.data)
                    this.listVacPorEnf = response.data;
                    console.log("List Vacunados Por Enf: ", this.listVacPorEnf);
                    this.setGrafica1();
                }))


        },
        cargarDiaMesAnio() {
            axios.post("http://localhost:8080/grupo15-services/rest/monitor/vacunados", {
                enfermedad: this.IdEnf.toString(),
                plan: this.IdPlan.toString(),
                vacuna: this.IdVac.toString(),

            })
                .then((response => {
                    console.log("GET vacunas: ", response.data)
                    this.nroDia = response.data.dia;
                    console.log("Dia: ", response.data.dia)
                    this.nroMes = response.data.mes;
                    console.log("Mes: ", response.data.mes)
                    this.nroAnio = response.data.anio;
                    console.log("Año: ", response.data.anio)

                }))

        },

        Grafica2() {
            // Obtener una referencia al elemento canvas del DOM
            const $grafica2 = document.querySelector("#grafica2");
            // Las etiquetas son las porciones de la gráfica
            console.log("Entre a grafica2");

            for (i in this.listVacPorVac) {

                this.etiquetas2.push(this.listVacPorVac[i].idVacuna)
            };

            for (i in this.listVacPorVac) {

                this.valores2.push(this.listVacPorVac[i].vacunados)
            };

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


