package smoothsail.operations.git.model

data class GitBranchDetails(
    val branchName: String,
    val lastCommitHash: String
)