angular.module('odontoFacil', ['ngRoute', 'ngMaterial', 'ngTable']).constant("consts", {
	BASE_URL: "http://localhost:8080"
})
.config(['$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {		
		$routeProvider.
			when('/home', { 
				templateUrl: "pages/dashboard.html",
				controller: "dashboardController",
				controllerAs: "ctrl"
			}).when('/cliente', { 
				templateUrl: "pages/cadastroCliente.html",
				controller: "clienteController",
				controllerAs: "ctrl"
			}).when('/editarCliente', { 
				templateUrl: "pages/cadastroCliente.html",
				controller: "clienteController",
				controllerAs: "ctrl"
			}).when('/agenda', { 
				templateUrl: "pages/agenda.html",
				controller: "agendaController",
				controllerAs: "ctrl"
			}).when('/consultarCliente', { 
				templateUrl: "pages/consultarCliente.html",
				controller: "clienteController",
				controllerAs: "ctrl"
			}).when('/cadastrarFuncionario', { 
				templateUrl: "pages/cadastroFuncionario.html",
				controller: "funcionarioController",
				controllerAs: "ctrl"			
			}).when('/consultarFuncionario', { 
				templateUrl: "pages/consultarFuncionario.html",
				controller: "funcionarioController",
				controllerAs: "ctrl"			
			}).when('/index', { 
				templateUrl: "index.html",
				controller: "loginController",
				controllerAs: "ctrl"			
			}).otherwise({redirectTo: '/'});
		
		$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
		
	}]).run(['$rootScope', function($rootScope){
		// start watching when the app runs. also starts the Keepalive service by default.
		
		
	}]);