document.addEventListener('DOMContentLoaded', function() {
    // Set API base URL
    const API_BASE_URL = '/api';
    
    // Get DOM elements
    const departmentIdInput = document.getElementById('departmentId');
    const searchBtn = document.getElementById('searchBtn');
    const employeeResults = document.getElementById('employeeResults');
    
    // Add event listener to search button
    searchBtn.addEventListener('click', function() {
        const departmentId = departmentIdInput.value.trim();
        
        if (departmentId) {
            fetchEmployeesByDepartment(departmentId);
        } else {
            showError('Please enter a Department ID');
        }
    });
    
    // Function to fetch employees by department ID
    function fetchEmployeesByDepartment(departmentId) {
        fetch(`${API_BASE_URL}/employees/department/${departmentId}`)
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.message || `Department with ID ${departmentId} not found`);
                    });
                }
                return response.json();
            })
            .then(employees => {
                displayEmployees(employees);
            })
            .catch(error => {
                showError(error.message);
            });
    }
    
    // Function to display employees in a table
    function displayEmployees(employees) {
        if (!employees || employees.length === 0) {
            employeeResults.innerHTML = `
                <div class="alert alert-info">
                    No employees found in this department.
                </div>
            `;
            return;
        }
        
        let tableHtml = `
            <div class="card">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0">Employees</h5>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-striped table-bordered mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Position</th>
                                    <th>Salary</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
        `;
        
        employees.forEach(employee => {
            tableHtml += `
                <tr>
                    <td>${employee.id}</td>
                    <td>${employee.name}</td>
                    <td>${employee.email}</td>
                    <td>${employee.position}</td>
                    <td>$${employee.salary.toFixed(2)}</td>
                    <td>
                        <a href="employee-detail.html?id=${employee.id}" class="btn btn-sm btn-primary">View Details</a>
                    </td>
                </tr>
            `;
        });
        
        tableHtml += `
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        `;
        
        employeeResults.innerHTML = tableHtml;
    }
    
    // Function to show error message
    function showError(message) {
        employeeResults.innerHTML = `
            <div class="alert alert-danger">
                ${message}
            </div>
        `;
    }
}); 