<script>
    export let onCreate = (poll) => {};

    let userId = null;
    let question = "";
    let optionInput = "";
    let options = [];

    function addOption() {
        if (optionInput.trim() !== "") {
            options = [...options, { text: optionInput, votes: 0 }];
            optionInput = "";
        }
    }

    function createPoll() {
        if (userId && question.trim() && options.length > 0) {
            const formattedPoll = {
                userId,
                question,
                options: options.map((opt) => opt.text), // strip out "votes"
            };
            onCreate(formattedPoll);
            userId = null;
            question = "";
            options = [];
        }
    }
</script>

<div class="p-4 border rounded-xl shadow">
    <h2 class="font-bold mb-2 text-lg">Create a new poll</h2>
    <div class="flex gap-4 items-end mb-4">
        <label class="block mb-2">
            Your User ID:
            <input
                class="border p-1 w-full rounded"
                bind:value={userId}
                placeholder="Enter your user ID"
            />
        </label>
    </div>

    <div class="flex gap-2">
        <label class="block mb-2">
            Question:
            <input
                class="border p-1 w-full rounded"
                bind:value={question}
                placeholder="Enter your poll question"
            />
        </label>
    </div>

    <div class="flex gap-2">
        <label class="block mb-2">
            Add option:
            <input
                class="border p-1 flex-grow rounded"
                bind:value={optionInput}
                placeholder="Enter option"
                on:keydown={(e) => e.key === "Enter" && addOption()}
            />
            <button
                class="bg-blue-500 text-white px-2 rounded"
                on:click={addOption}
            >
                Add
            </button>
        </label>
    </div>

    {#if options.length > 0}
        <ul class="mb-2 list-disc ml-5">
            {#each options as opt}
                <li>{opt.text}</li>
            {/each}
        </ul>
    {/if}

    <button
        class="bg-green-500 text-white px-4 py-1 rounded mt-2"
        on:click={createPoll}
    >
        Create Poll
    </button>
</div>
