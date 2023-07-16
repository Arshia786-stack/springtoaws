document.getElementById("uploadForm").addEventListener("submit", function(event) {
    event.preventDefault();
    
    var form = event.target;
    var formData = new FormData(form);
  
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/upload", true);
    xhr.onload = function() {
      if (xhr.status === 200) {
        document.getElementById("message").textContent = "File uploaded successfully.";
        form.reset();
      } else {
        document.getElementById("message").textContent = "Error uploading file.";
      }
    };
    xhr.send(formData);
  });
  