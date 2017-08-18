angular.module('odontoFacil').controller("funcionarioController", ['$mdDialog', 'funcionarioFactory', 'NgTableParams', '$scope',
	function($mdDialog, funcionarioFactory, NgTableParams, $scope) {
	var ctrl = this;

	ctrl.funcionarios = [];
	ctrl.funcionario = {};
	
	$scope.$watch(function () { return ctrl.funcionarios; }, function (newValue, oldValue) {
		ctrl.tableParams = new NgTableParams({ count: 10, sorting: { nomeCompleto: "asc" } }, { counts: [], dataset: ctrl.funcionarios });
	});
	
	if (funcionarioFactory.isEditandoFuncionario()) {
		ctrl.funcionario = funcionarioFactory.getFuncionario();		
	}

	// primeiro parametro, sucesso, segundo parametro erro.
	ctrl.listarFuncionarios = function() {
		funcionarioFactory.listarFuncionarios().then(function successCallback(response) {
			ctrl.funcionarios = response.data;
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});

	}
	
	ctrl.excluirFuncionarios = function(funcionario) {
		console.log(cliente);
		var confirm = $mdDialog.confirm()
		.title('Atenção')
		.textContent('Todas as informações do paciente, incluindo os prontuários, serão perdidas. Tem certeza que deseja continuar?')				
		.ok('Sim')
		.cancel('Não');
		
		funcionarioFactory.excluirFuncionarios(funcionario).then(function successCallback(response) {
			ctrl.listarFuncionarios();
			ctrl.funcionarios = response.data;
			console.log(response.data);
			console.log(response.status);
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});
		
	}
	
	ctrl.editarFuncionario = function(funcionario) {
		funcionarioFactory.setFuncionario(funcionario);
		$location.path("/editarFuncionario");
	};
	
	
	ctrl.salvarFuncionarios = function(funcionario) {
		funcionarioFactory.salvarFuncionarios(funcionario).then(function successCallback(response) {
			$mdDialog.show(
					$mdDialog.alert()
						.clickOutsideToClose(true)
						.title('Cadastro de Funcionário')
						.textContent('Funcionário cadastrado com sucesso!')
						.ariaLabel('Alerta')
						.ok('Ok')						
				);	
			ctrl.funcionario = {};	
			ctrl.funcionarios = response.data;
			$scope.frmFuncionario.$setPristine();
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});
		
	}
	
	ctrl.listarFuncionarios();
}]);