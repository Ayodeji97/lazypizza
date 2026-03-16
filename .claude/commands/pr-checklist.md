# LazyPizza PR Checklist

You are running a pre-PR quality check for **LazyPizza** — a Kotlin + Jetpack Compose Android app following Clean Architecture, MVI, and Koin DI. Work through every section below and produce a final report.

---

## Step 1 — Discover changed files

Run `git diff --name-only main...HEAD` (or `git diff --name-only HEAD~1` if on main) to get the list of changed files. Keep this list — every subsequent check focuses on these files only, not the whole codebase.

---

## Step 2 — Run automated checks

Run both commands and capture the output:

```bash
./gradlew lint 2>&1
./gradlew testDebug 2>&1
```

Report results:
- **Lint** — list every `Error` or `Warning` from changed files. Ignore warnings in unchanged files.
- **Tests** — list every failing test by name. If all pass, state the count.

If either command fails to compile, stop and report the compilation error. Do not proceed until the code compiles.

---

## Step 3 — MVI conventions

For each changed ViewModel (`*ViewModel.kt`), read the file and check:

| # | Rule | How to check |
|---|------|-------------|
| 3a | State is only mutated via `_state.update { }` | No `_state.value = ...` assignments |
| 3b | All `onAction` branches are handled | `when (action)` has no `else` fallback hiding unhandled actions |
| 3c | One-time events go through `Channel<Event>` | No `_state.update` used for navigation, toasts, or dialogs |
| 3d | `viewModelScope.launch` is used for suspend calls | No `runBlocking` or `GlobalScope` |
| 3e | Flows are collected with `launchIn(viewModelScope)` or `collectAsEffect` in the screen | No `collect {}` inside `init {}` without a scope |

---

## Step 4 — Result handling

For each changed file that calls a repository method, check:

| # | Rule | How to check |
|---|------|-------------|
| 4a | Every `Result<T, E>` return is branched on `Success`/`Error` | No `.let { }` or ignored returns on `Result`-typed calls |
| 4b | Error branches send a user-visible event | `Result.Error` blocks either send a `Channel` event or update state — never silent |
| 4c | No exceptions used for control flow | No bare `try/catch` wrapping repository calls (the `Result` type handles this) |

---

## Step 5 — Hardcoded strings

Read each changed Composable file (`*Screen.kt`, `*Content.kt`, `*Card.kt`, `*Component.kt`).

Flag any `Text(` call where the string argument is a raw string literal (e.g. `Text("Hello")`) instead of:
- `stringResource(R.string.some_key)`, or
- a `UiText` value resolved via `.asString()`

Also check: any new `UiText.StringResource(R.string.some_key)` — verify that `some_key` exists in `app/src/main/res/values/strings.xml`.

---

## Step 6 — Koin DI wiring

For each new class added in the changed files:

| Class type | Expected registration | Where to look |
|---|---|---|
| `ViewModel` | `viewModelOf(::SomeViewModel)` | Feature's `*Module.kt` in `di/` |
| Repository `impl` | `singletonOf(::SomeRepositoryImpl) bind SomeRepository::class` | Feature's `*Module.kt` |
| Use case | `factoryOf(::SomeUseCase)` | Feature's `*Module.kt` |

Check that the module itself is included in `LazyPizzaApplication` or the parent `AppModule`.

---

## Step 7 — Navigation wiring

If any new screen was added (new `*Screen.kt` composable):

1. Check `NavigationRoute.kt` — does a new `@Serializable` route object/data class exist for it?
2. Check `NavigationRoot.kt` — is the new route wired with a `composable<RouteClass> { ... }` block?
3. Check that the screen is reachable — at least one `navController.navigate(RouteClass(...))` call exists somewhere.

---

## Step 8 — TODOs and placeholders

Search changed files for:
- `// TODO`
- `// FIXME`
- `// HACK`
- `throw NotImplementedError`
- `error("not implemented")`

List each one with its file and line number. TODOs are acceptable if they reference a future milestone — flag them so the author is aware, but do not block the PR.

---

## Step 9 — Final report

Produce a concise report in this format:

```
## PR Checklist Report

### ✅ Automated checks
- Lint: [X errors, Y warnings in changed files / Clean]
- Tests: [X/Y passing / all passing]

### ✅/⚠️/❌ MVI conventions
[List any violations, or "All ViewModels follow MVI conventions"]

### ✅/⚠️/❌ Result handling
[List any unhandled Result branches, or "All repository calls handle both Result branches"]

### ✅/⚠️/❌ Hardcoded strings
[List any raw string literals in Composables, or "No hardcoded strings found"]

### ✅/⚠️/❌ Koin DI
[List any unregistered classes, or "All new classes are registered in Koin modules"]

### ✅/⚠️/❌ Navigation
[List any unwired screens, or "No new screens / All new screens wired"]

### ⚠️ TODOs
[List any TODOs with file:line, or "No TODOs in changed files"]

---
**Overall: READY TO MERGE / NEEDS FIXES**
```

Use:
- ✅ — check passed, nothing to fix
- ⚠️ — issues worth noting but not blocking
- ❌ — must be fixed before merging
