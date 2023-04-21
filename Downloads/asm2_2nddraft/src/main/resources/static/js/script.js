var app = new Vue({
el: "#app",
        data: {
        workMode: 'Disconnect', //
                currentPage: 0, //the current page number to display in website
                pageCnt: 0, //the total page count for to do list table
                maxItemsPerPage: 5, //maximum records number that can show in 1 page
                rows: [], // store records of pending status
                completedForm: [], // store records of completed status
                full: [], // store all records
                editForm: {
                },
                newItem: {
                "id": 0,
                        "item": '',
                        "status": 'Pending'
                },
        },
        created: async function () {
            //Fetch the todo list and output in the table, and set the work mode to online
            await fetch('http://localhost:80/api/todolists/all').then((response) => response.json())
                .then((data) => {
                this.full = data;
                        this.workMode = "Online";
            });
                //Set the total page number based on the to do list table only, the paging only work on to do list table
                this.pageCnt = Math.ceil(this.rows.length / this.maxItemsPerPage);
                if (localStorage.getItem("session") != null) {
                    //Check for difference between the database content and localStorage
                    if (JSON.stringify(this.full) === localStorage.getItem("session")) {
                        alert("No Changes since last time connection");
                    } 
                    else {
                        //Update the localStorage items into Server
                        for (let i = 0; i < JSON.parse(localStorage.getItem("session")).length; i++) {
                            var json = JSON.parse(localStorage.getItem("session"));
                            await fetch("http://localhost:80/api/todolists", {
                                method: "POST",
                                headers: {
                                "Content-Type": "application/json"
                                },
                                body:
                                JSON.stringify(json[i])
                            });
                        }
                        //Fetch the todo list and output in the table, and set the work mode to online
                        await fetch('http://localhost:80/api/todolists/all').then((response) => response.json())
                            .then((data) => {
                            this.full = data;
                            this.workMode = "Online";
                            });
                        }

                }
                else {
                    console.log("localStorage is null!");
                }
                //fetch the data from database again
                await fetch("http://localhost:80/api/todolists?status=Pending")
                    .then((response) => response.json())
                    .then((data) => this.rows = data);
                await fetch("http://localhost:80/api/todolists?status=Completed")
                    .then((response) => response.json())
                    .then((data) => this.completedForm = data);
        },
        mounted: function () {
            //check the connection in every 5 seconds
            setInterval(function () {                                                          
            fetch('http://localhost:80/api/todolists/all').then(() => app._data.workMode = 'Online')
                .catch(() => app._data.workMode = 'Offline');
                fetch("http://localhost:80/api/todolists?status=Pending")
                .then((response) => response.json())
                .then((data) => this.rows = data);
                fetch("http://localhost:80/api/todolists?status=Completed")
                .then((response) => response.json())
                .then((data) => this.completedForm = data);
            }, 5000);
        },
        methods: {
            //the save button event for editing the form 
            saveList() {
                fetch("http://localhost:80/api/todolists/" + this.editForm.id, {
                method: "PUT",
                headers: {
                "Content-Type": "application/json",
                },
                body: JSON.stringify(this.editForm),
            })
                this.editForm = {};
            },
            completeList(row) {
                app._data.completedForm.push({id: row.id, item: row.item, status: 'Completed'});
                console.log(this.completedForm);
                row.status = "Completed";
                fetch("http://localhost:80/api/todolists/" + row.id, {
                method: "PUT",
                        headers: {
                        "Content-Type": "application/json",
                        },
                        body: JSON.stringify(row),
                })
                const index = this.rows.indexOf(row);
                if (index > - 1) { // only splice array when item is found
                    this.rows.splice(index, 1); // 2nd parameter means remove one item only
                }
            },
            editList(row) {
                this.editForm = row;
                console.log(this.editForm);
            },
            deleteList(row) {
                fetch("http://localhost:80/api/todolists/" + row.id, {
                    method: "DELETE"
                })
                const index = this.rows.indexOf(row);
                if (index > - 1) { // only splice array when item is found
                    this.rows.splice(index, 1); // 2nd parameter means remove one item only
                }
            },
            addList() {
                this.newItem.id = this.rows.length + this.completedForm.length + 1;
                console.log(this.newItem);
                fetch("http://localhost:80/api/todolists", {
                method: "POST",
                        headers: {
                        "Content-Type": "application/json",
                        },
                        body: JSON.stringify(this.newItem),
                })
                app._data.rows.push({id: this.newItem.id, item: this.newItem.item, status: 'Pending'});
                newItem = {
                "id": 0,
                        "item": "",
                        "status": "Pending"
                };
                document.getElementsByClassName("input-box")[0].value = "";
            },
            changeList(row) {
                app._data.rows.push({id: row.id, item: row.item, status: 'Pending'});
                console.log(this.rows);
                row.status = "Pending";
                fetch("http://localhost:80/api/todolists/" + row.id, {
                method: "PUT",
                        headers: {
                        "Content-Type": "application/json",
                        },
                        body: JSON.stringify(row),
                })
                const index = this.completedForm.indexOf(row);
                if (index > - 1) { // only splice array when item is found
                    this.completedForm.splice(index, 1); // 2nd parameter means remove one item only
                }
            },
            showCompletedForm() {
                let complete = document.getElementsByClassName("completed")[0];
                let button = document.getElementsByClassName("complete-button")[0];
                if (button.innerText === "show completed") {
                    button.innerText = "hide completed";
                    complete.style.display = "block";
                }       
                else {
                    button.innerText = "show completed";
                    complete.style.display = "none";
                }
            },
            async saveLocal() {
                //Save current table contents into localStorage
                this.full = [];
                this.full = [...this.rows, ...this.completedForm];
                localStorage.setItem("session", JSON.stringify(this.full));
                alert("Saved");
            },
            setPage(page) {                  //Go to the targetted page
                this.currentPage = page;
            }
        },
        computed: {
            currentPageItems() {             //Parse to-do items in order in current page
                const start = this.currentPage * this.maxItemsPerPage;
                const end = start + this.maxItemsPerPage;
                return this.rows.slice(start, end);
            }
        },
        watch: {
            rows() {                         //Total number of pages
                this.pageCnt = Math.ceil(this.rows.length / this.maxItemsPerPage);
            },
            currentPage() {                  //Current page number
                this.$emit('page-changed', this.currentPage);
            }
        }
})
