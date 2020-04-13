package smoothsail.operations.git.api

interface GithubApi {
  fun pullCheckoutBranch(repository: String, branch: String): String // returns the hash of the branch
}