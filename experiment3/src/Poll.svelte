<script>
    export let question;
    export let options = []; // Each option should be { id, text, votes }
    export let onVote = (optionId) => {}; // We now pass the id instead of index
</script>

<div class="p-4 border rounded-xl shadow">
    <h2 class="font-bold text-lg mb-2">{question}</h2>

    <ul>
        {#each options as option}
            <li class="flex justify-between items-center mb-2">
                <span>{option.text}</span>
                <button
                    class="bg-blue-500 text-white px-2 rounded"
                    on:click={() => onVote(option.id)}
                >
                    Vote ({option.votes})
                </button>
            </li>
        {/each}
    </ul>
</div>
