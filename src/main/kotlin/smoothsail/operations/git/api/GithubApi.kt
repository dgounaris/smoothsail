package smoothsail.operations.git.api

interface GithubApi {
  //TODO replace strings with custom string-like objects for defining semantically that these are hashes
  fun rebaseAndPush(repository: String, origin: String, target: String, newBranchName: String): String //returns the hash of the branch
  fun pullCheckoutBranch(repository: String, branch: String): String // returns the hash of the branch
}