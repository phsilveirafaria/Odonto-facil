angular.module('odontoFacil').controller("funcionarioController", ['$mdDialog', 'funcionarioFactory', 'NgTableParams', '$scope',
	function($mdDialog, funcionarioFactory, NgTableParams, $scope) {
	var ctrl = this;

	ctrl.funcionarios = [];
	ctrl.funcionario = {};
	
	$scope.$watch(function () { return ctrl.funcionarios; }, function (newValue, oldValue) {
		ctrl.tableParams = new NgTableParams({ count: 10, sorting: { nomeCompleto: "asc" } }, { counts: [], dataset: ctrl.funcionarios });
	});
	
	if (cadastroFuncionarioFactory.isEditandoFuncionario()) {
		ctrl.funcionario = cadastroFuncionarioFactory.getFuncionario();		
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
		funcionarioFactory.excluirFuncionarios(funcionario).then(function successCallback(response) {
			ctrl.funcionarios = response.data;
			console.log(response.data);
			console.log(response.status);
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});
		
	}
	
	ctrl.editarFuncionario = function(funcionario) {
		cadastroFuncionarioFactory.setFuncionario(funcionario);
		$location.path("/editarFuncionario");
	};
	
	
	ctrl.salvarFuncionarios = function(funcionario) {
		funcionarioFactory.salvarFuncionarios(funcionario).then(function successCallback(response) {
			$mdDialog.show(
					$mdDialog.alert()
						.clickOutsideToClose(true)
						.title('Cadastro de Cliente')
						.textContent('Funcionário cadastrado com sucesso!')
						.ariaLabel('Alerta')
						.ok('Ok')						
				);	
			ctrl.paciente = {};	
			ctrl.funcionarios = response.data;
			$scope.frmFuncionario.$setPristine();
		}, function errorCallback(response) {
			console.log(response.data);
			console.log(response.status);
		});
		
	}
	
	ctrl.listarFuncionarios();
}]);