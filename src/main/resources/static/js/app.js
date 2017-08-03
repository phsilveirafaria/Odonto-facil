angular.module('odontoFacil', ['ngRoute', 'ngTable'])
.config(['$routeProvider', function($routeProvider) {		
		$routeProvider.
			when('/home', { 
				templateUrl: "home.html",
				//controller: "indexCtrl",
			}).when('/cliente', { 
				templateUrl: "pages/cadastroCliente.html",
				controller: "clienteController",
				controllerAs: "ctrl"
			}).when('/consultarCliente', { 
				templateUrl: "pages/consultarCliente.html",
				controller: "clienteController",
				controllerAs: "ctrl"
			}).when('/consultarCliente', { 
				templateUrl: "pages/consultarCliente.html",
				controller: "clienteController",
				controllerAs: "ctrl"			
			}).when('/index', { 
				templateUrl: "index.html",
				controller: "loginController",
				controllerAs: "ctrl"			
			}).otherwise({redirectTo: '/'});		
	}]).run(['$rootScope', function($rootScope){
		// start watching when the app runs. also starts the Keepalive service by default.
		

		
	}]);