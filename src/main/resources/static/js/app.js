angular.module('odontoFacil', ['ngRoute', 'ngMaterial', 'ngTable']).constant("consts", {
	BASE_URL: "http://localhost:8080"
})
.config(['$routeProvider', function($routeProvider) {		
		$routeProvider.
			when('/home', { 
				templateUrl: "pages/dashboard.html",
			}).when('/cliente', { 
				templateUrl: "pages/cadastroCliente.html",
				controller: "clienteController",
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
	}]).run(['$rootScope', function($rootScope){
		// start watching when the app runs. also starts the Keepalive service by default.
		

		
	}]);