document.addEventListener('DOMContentLoaded', function() {
    // Set API base URL
    const API_BASE_URL = '/api';
    
    // Get DOM elements
    const form = document.getElementById('employeeForm');
    const idInput = document.getElementById('id');
    const nameInput = document.getElementById('name');
    const emailInput = document.getElementById('email');
    const positionInput = document.getElementById('position');
    const salaryInput = document.getElementById('salary');
    const departmentIdInput = document.getElementById('departmentId');
    const backBtn = document.getElementById('backBtn');
    const deleteBtn = document.getElementById('deleteBtn');
    
    // Get employee ID from URL params
    const urlParams = new URLSearchParams(window.location.search);
    const employeeId = urlParams.get('id');
    
    // Load employee data if ID is provided
    if (employeeId) {
        loadEmployeeData(employeeId);
    } else {
        window.location.href = 'index.html';
    }
    
    // Add event listener to form submit
    form.addEventListener('submit', function(e) {
        e.preventDefault();
        saveEmployeeData();
    });
    
    // Add event listener to back button
    backBtn.addEventListener('click', function() {
        const departmentId = departmentIdInput.value;
        window.location.href = `index.html${departmentId ? '?departmentId=' + departmentId : ''}`;
    });
    
    // Add event listener to delete button
    deleteBtn.addEventListener('click', function() {
        if (confirm('Are you sure you want to delete this employee?')) {
            deleteEmployee();
        }
    });
    
    // Function to load employee data
    function loadEmployeeData(id) {
        fetch(`${API_BASE_URL}/employees/${id}`)
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.message || `Employee with ID ${id} not found`);
                    });
                }
                return response.json();
            })
            .then(employee => {
                // Populate form fields
                idInput.value = employee.id;
                nameInput.value = employee.name;
                emailInput.value = employee.email;
                positionInput.value = employee.position;
                salaryInput.value = employee.salary;
                departmentIdInput.value = employee.departmentId;
            })
            .catch(error => {
                alert(error.message);
                window.location.href = 'index.html';
            });
    }
    
    // Function to save employee data
    function saveEmployeeData() {
        const employeeData = {
            id: idInput.value,
            name: nameInput.value,
            email: emailInput.value,
            position: positionInput.value,
            salary: parseFloat(salaryInput.value),
            departmentId: departmentIdInput.value
        };
        
        fetch(`${API_BASE_URL}/employees/${employeeData.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(employeeData)
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => {
                    throw new Error(err.message || 'Failed to update employee');
                });
            }
            return response.json();
        })
        .then(() => {
            alert('Employee updated successfully!');
            window.location.href = `index.html?departmentId=${employeeData.departmentId}`;
        })
        .catch(error => {
            alert(error.message);
        });
    }
    
    // Function to delete employee
    function deleteEmployee() {
        const id = idInput.value;
        const departmentId = departmentIdInput.value;
        
        fetch(`${API_BASE_URL}/employees/${id}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => {
                    throw new Error(err.message || 'Failed to delete employee');
                });
            }
            return response;
        })
        .then(() => {
            alert('Employee deleted successfully!');
            window.location.href = `index.html?departmentId=${departmentId}`;
        })
        .catch(error => {
            alert(error.message);
        });
    }
}); 