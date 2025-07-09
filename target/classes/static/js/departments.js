document.addEventListener('DOMContentLoaded', function() {
    // Set API base URL
    const API_BASE_URL = '/api';
    
    // Get DOM elements
    const departmentsTableBody = document.getElementById('departmentsTableBody');
    const generateReportBtn = document.getElementById('generateReportBtn');
    
    // Load departments when page loads
    loadDepartments();
    
    // Add event listener to generate report button
    generateReportBtn.addEventListener('click', function() {
        window.open(`${API_BASE_URL}/departments/report`, '_blank');
    });
    
    // Function to load departments
    function loadDepartments() {
        fetch(`${API_BASE_URL}/departments`)
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.message || 'Failed to load departments');
                    });
                }
                return response.json();
            })
            .then(departments => {
                displayDepartments(departments);
            })
            .catch(error => {
                showError(error.message);
            });
    }
    
    // Function to display departments in the table
    function displayDepartments(departments) {
        if (!departments || departments.length === 0) {
            departmentsTableBody.innerHTML = `
                <tr>
                    <td colspan="4" class="text-center">No departments found.</td>
                </tr>
            `;
            return;
        }
        
        let tableRows = '';
        
        departments.forEach(department => {
            tableRows += `
                <tr>
                    <td>${department.id}</td>
                    <td>${department.name}</td>
                    <td>${department.location}</td>
                    <td>
                        <a href="index.html?departmentId=${department.id}" class="btn btn-sm btn-primary me-2">View Employees</a>
                    </td>
                </tr>
            `;
        });
        
        departmentsTableBody.innerHTML = tableRows;
        
        // Add event listener to department links to pre-fill the department ID
        document.querySelectorAll('a[href^="index.html?departmentId="]').forEach(link => {
            link.addEventListener('click', function(e) {
                // We don't need to prevent default because we actually want to navigate
                const url = new URL(this.href);
                const departmentId = url.searchParams.get('departmentId');
                localStorage.setItem('selectedDepartmentId', departmentId);
            });
        });
    }
    
    // Function to show error message
    function showError(message) {
        departmentsTableBody.innerHTML = `
            <tr>
                <td colspan="4" class="text-center text-danger">${message}</td>
            </tr>
        `;
    }
}); 