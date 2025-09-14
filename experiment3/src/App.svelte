<script>
  import PollCreator from "./PollCreator.svelte";
  import Poll from "./Poll.svelte";
  import { onMount } from "svelte";
  import UserCreator from "./UserCreator.svelte";

  let polls = [];
  let users = [];
  let currentUser = null;

  onMount(async () => {
    const response = await fetch("/polls");
    polls = await response.json();
  });

  async function handleCreatePoll(poll) {
    const response = await fetch("/polls", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(poll),
    });
    const newPoll = await response.json();
    polls = [...polls, newPoll];
  }

  async function handleVote(pollIndex, optionId) {
    const poll = polls[pollIndex];
    const res = await fetch(`/votes`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ pollId: poll.id, optionId }),
    });
    const updatedPoll = await res.json();
    polls[pollIndex] = updatedPoll;
  }

  async function handleCreateUser(user) {
    const response = await fetch("/users", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(user),
    });
    const newUser = await response.json();
    users = [...users, newUser];
    currentUser = newUser;
  }
</script>

<main class="flex gap-8 items-start">
  <div class="flex-1 space-y-6">
    <UserCreator onCreate={handleCreateUser} />
    <PollCreator onCreate={handleCreatePoll} />

    {#each polls as poll, i}
      <Poll
        question={poll.question}
        options={poll.options.map((option) => ({
          id: option.id,
          text: option.caption,
          votes: option.votes,
        }))}
        onVote={(optionId) => handleVote(i, optionId)}
      />
    {/each}
  </div>

  <div class="w-1/3">
    {#if currentUser}
      <div class="p-4 border rounded-xl shadow bg-green-50">
        <h3 class="font-bold text-lg">User Created</h3>
        <p><strong>Username:</strong> {currentUser.username}</p>
        <p><strong>Email:</strong> {currentUser.email}</p>
        <p>
          <strong>User ID:</strong> <code>{currentUser.id}</code>
          <button
            class="bg-gray-200 px-2 py-1 rounded ml-2"
            on:click={() => navigator.clipboard.writeText(currentUser.id)}
          >
            Copy
          </button>
        </p>
      </div>
    {/if}
  </div>
</main>
