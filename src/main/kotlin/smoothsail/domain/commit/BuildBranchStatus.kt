package smoothsail.domain.commit

enum class BuildBranchStatus {
  PENDING,
  SUCCESS,
  FAILURE,
  MERGED,
  CONFLICT
}