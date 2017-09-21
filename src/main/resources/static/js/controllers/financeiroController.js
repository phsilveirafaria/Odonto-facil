angular.module('odontoFacil').controller("financeiroController", ['$mdDialog', 'financeiroFactory', 'NgTableParams', '$scope', '$location', '$http' ,
	function($mdDialog, financeiroFactory, NgTableParams, $scope, $location, $http) {
	var ctrl = this;
	
	
	$scope.$watch(function () { return ctrl.funcionario; }, function (newValue, oldValue) {
	});
}]);