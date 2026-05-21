const tableBody = document.getElementById('documentsTableBody');
const btnFilter = document.getElementById('btnFilter');
const btnClear = document.getElementById('btnClear');
const filterSelect = document.getElementById('filterSelect');
const filterInput = document.getElementById('filterInput');
const currentFilterText = document.getElementById('currentFilterText');

document.addEventListener('DOMContentLoaded', () => {
    fetchDocuments();
});

btnFilter.addEventListener('click', () => {
    const type = filterSelect.value;
    const value = filterInput.value.trim();

    if (value === '') {
        alert('Please enter a filter value first!');
        return;
    }

    fetchDocuments(type, value);
});

btnClear.addEventListener('click', () => {
    filterInput.value = '';
    fetchDocuments();
});

function fetchDocuments(filterType = '', value = '') {
    let url = 'get_documents.php';
    if (filterType && value) {
        url += `?filter_type=${encodeURIComponent(filterType)}&value=${encodeURIComponent(value)}`;
    }

    // This is the actual asynchronous AJAX call
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            renderTable(data);
            updateFilterDisplay(filterType, value);
        })
        .catch(error => {
            console.error('AJAX Error:', error);
            tableBody.innerHTML = `<tr><td colspan="6" style="text-align: center; color: red;">Failed to load documents. Check console.</td></tr>`;
        });
}

function renderTable(documents) {
    tableBody.innerHTML = '';

    if (documents.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="6" style="text-align: center;">No documents matched your criteria.</td></tr>`;
        return;
    }

    documents.forEach(doc => {
        const row = document.createElement('tr');

        row.innerHTML = `
            <td>${escapeHTML(doc.title)}</td>
            <td>${escapeHTML(doc.author)}</td>
            <td>${doc.number_of_pages}</td>
            <td>${escapeHTML(doc.document_type)}</td>
            <td>${escapeHTML(doc.document_format)}</td>
            <td>
                <a href="edit.html?id=${doc.id}" class="btn action">Edit</a>
                <a href="delete.html?id=${doc.id}" class="btn danger">Delete</a>
            </td>
        `;

        tableBody.appendChild(row);
    });
}

function updateFilterDisplay(type, value) {
    if (type && value) {
        const cleanType = type === 'document_type' ? 'Type' : 'Format';
        currentFilterText.textContent = `${cleanType} = "${value}"`;
    } else {
        currentFilterText.textContent = 'None (Showing all records)';
    }
}

//Security Extra: Basic XSS protection when injecting database strings into HTML
function escapeHTML(str) {
    return str.replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
}