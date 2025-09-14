<script>
    export let onCreate = (user) => {};

    let username = "";
    let email = "";

    function createUser() {
        if (username.trim() && email.trim()) {
            const formattedUser = {
                username,
                email,
            };
            onCreate(formattedUser);
            username = "";
            email = "";
        }
    }
</script>

<div class="p-4 border rounded-xl shadow">
    <h2 class="font-bold mb-2 text-lg">Create a new user</h2>
    <div class="flex gap-4 items-end mb-4">
        <label class="block mb-2">
            Username:
            <input
                class="border p-1 w-full rounded"
                bind:value={username}
                placeholder="Enter your username"
            />
        </label>
    </div>
    <div class="flex gap-4 items-end mb-4">
        <label class="block mb-2">
            Email:
            <input
                class="border p-1 w-full rounded"
                bind:value={email}
                placeholder="Enter your email"
            />
        </label>
    </div>
    <div class="flex gap-4 items-end mb-4">
        <button
            class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
            on:click={createUser}
        >
            Create User
        </button>
    </div>
</div>
